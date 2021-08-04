package com.example.logintext.protector;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pro_SafeZoneActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap; // 구글 맵 객체
    private Button btn;
    private ImageButton back;
    private EditText zipcode, area;

    private Marker currentMarker = null;
    private Circle currentCircle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_safe);

        // 맵 객체 생성
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        btn = (Button) findViewById(R.id.safeRegi);
        zipcode = (EditText) findViewById(R.id.address);
        area = (EditText) findViewById(R.id.km);

        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pro_SafeZoneActivity.this, Pro_LocationActivity.class));
                finish();
            }
        });

        mapFragment.getMapAsync(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddMarker();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        this.mMap = mMap;

        //지도타입 - 일반
        this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //기본위치(서울)
        LatLng position = new LatLng(37.56, 126.97);

        //화면중앙의 위치와 카메라 줌비율
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
    }

    public void onAddMarker() {
        String addrResult = zipcode.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;
        if (currentMarker != null) currentMarker.remove();
        if (currentCircle != null) currentCircle.remove();
        try {
            addresses = geocoder.getFromLocationName("대한민국 "+addrResult, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address addr = addresses.get(0);
        double lat = addr.getLatitude();
        double lon = addr.getLongitude();

        LatLng position = new LatLng(lat, lon);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference mReference = ref.child("protector").child(uid).child("safeZone");
        Map<Object, String> his = new HashMap<>();
        his.put("latitude", ""+lat);
        his.put("longitude", ""+lon);
        his.put("address", ""+addrResult);
        his.put("area", area.getText().toString());

        mReference.setValue(his);

        //나의위치 마커
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)   //마커위치
                .title("안전구역 기준")
                .snippet(addrResult);

        // 반경 'area'KM 원
        CircleOptions circleOptions = new CircleOptions().center(position) //원점
                .radius(Integer.parseInt(area.getText().toString())*1000)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#880000ff")); //배경색

        // 위도,경도
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
        currentMarker = mMap.addMarker(markerOptions);
        currentMarker.showInfoWindow();
        currentCircle = mMap.addCircle(circleOptions);
    }
}

