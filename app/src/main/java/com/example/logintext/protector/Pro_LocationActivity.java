package com.example.logintext.protector;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.logintext.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class Pro_LocationActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap; // 구글 맵 객체

    private ImageButton back;
    private Button area, history;
    private TextView address;
    private SupportMapFragment mapFragment;

    private Circle currentCircle = null;

    private String userAddr = "";

    private double userLat = 0, userLon = 0, distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_location_trace);

        // 맵 객체 생성
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        area = (Button) findViewById(R.id.button3);
        history = (Button) findViewById(R.id.button4);
        address = (TextView) findViewById(R.id.textView16);
        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pro_LocationActivity.this, Pro_MainActivity.class));
                finish();
            }
        });

        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pro_LocationActivity.this, Pro_SafeZoneActivity.class));
                finish();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pro_LocationActivity.this, Pro_GPSHistoryActivity.class));
                finish();
            }
        });

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        this.mMap = mMap;

        //지도타입 - 일반
        this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        ref.child("protector").child(uid).child("myUser").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            double latit, longit, area;

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String myUser = task.getResult().getValue().toString();

                DatabaseReference mReference = ref.child("user").child(myUser).child("gps");
                DatabaseReference safeReference = ref.child("protector").child(uid).child("safeZone");

                if (!myUser.equals("none")) {
                    safeReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.getValue().toString().equals("none")) {
                                latit = Double.parseDouble(snapshot.child("latitude").getValue().toString());
                                longit = Double.parseDouble(snapshot.child("longitude").getValue().toString());
                                area = Double.parseDouble(snapshot.child("area").getValue().toString());

                                if (currentCircle != null) currentCircle.remove();

                                LatLng position = new LatLng(latit, longit);

                                CircleOptions circleOptions = new CircleOptions().center(position) //원점
                                        .radius(area * 1000)      //반지름 단위 : m
                                        .strokeWidth(0f)  //선너비 0f : 선없음
                                        .fillColor(Color.parseColor("#880000ff")); //배경색

                                currentCircle = mMap.addCircle(circleOptions);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    mReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.getValue().toString().equals("none")) {
                                String lat, lon, addr;
                                for (DataSnapshot messageData : snapshot.getChildren()) {
                                    lat = messageData.child("latitude").getValue().toString();
                                    lon = messageData.child("longitude").getValue().toString();
                                    addr = messageData.child("address").getValue().toString();

                                    userLat = Double.parseDouble(lat);
                                    userLon = Double.parseDouble(lon);
                                    userAddr = addr;
                                }
                                Marker currentMarker = null;
                                if (currentMarker != null) currentMarker.remove();

                                LatLng position = new LatLng(userLat, userLon);

                                //나의위치 마커
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(position)   //마커위치
                                        .title("사용자 위치")
                                        .snippet(userAddr);

                                address.setText(userAddr);

                                currentMarker = mMap.addMarker(markerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
                                currentMarker.showInfoWindow();

                                safeReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(!task.getResult().getValue().toString().equals("none")) {
                                            double rad = 6372.8;

                                            double safeLat = Math.toRadians(latit - userLat);
                                            double safeLon = Math.toRadians(longit - userLon);

                                            double a = sin(safeLat / 2) * sin(safeLat / 2) + sin(safeLon / 2) * sin(safeLon / 2)
                                                    * cos(Math.toRadians(userLat)) * cos(Math.toRadians(latit));

                                            double b = 2 * asin(sqrt(a));
                                            distance = rad * b;

                                            if (distance > area) {
                                                Toast.makeText(getApplicationContext(), "안전구역을 벗어났습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "안전구역 이내입니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });

                            } else {
                                LatLng position = new LatLng(37.56, 126.97);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "사용자를 등록해주세요.", Toast.LENGTH_SHORT).show();
                    LatLng position = new LatLng(37.56, 126.97);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
                }
            }
        });
    }
}

