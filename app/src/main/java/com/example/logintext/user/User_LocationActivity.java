package com.example.logintext.user;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.logintext.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class User_LocationActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 20000;  // 10초
    private static final int FAST_UPDATE_INTERVAL_MS = UPDATE_INTERVAL_MS / 2;  // 5초
    private static final int MAX_WAIT_TIME = 120000;  // 60초
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private boolean needRequest = false;

    //필요한 권한 정의
    private String uid;
    private String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION};  // 외부 저장소

    private TextView address;
    private ImageButton back;

    private Location mCurrentLocation;
    private LatLng currentPosition;     // 마커

    private Circle currentCircle = null;

    private ClusterManager<MyItem> mClusterManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser user;

    private View mLayout;  // Snackbar 사용하기 위해서 View 필요


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.user_location_trace);

        mLayout = findViewById(R.id.layout_location);
        address = (TextView) findViewById(R.id.address);

        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_LocationActivity.this, User_MainActivity.class));
                finish();
            }
        });

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FAST_UPDATE_INTERVAL_MS)
                .setMaxWaitTime(MAX_WAIT_TIME);
        //.setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mClusterManager = new ClusterManager<>(this, mMap);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mReference.child("user").child(uid).child("myProtector").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String myPro = task.getResult().getValue().toString();
                if (!myPro.equals("none")) {
                    mReference.child("protector").child(myPro).child("safeZone").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                String lat = snapshot.child("latitude").getValue().toString();
                                String lon = snapshot.child("longitude").getValue().toString();
                                String area = snapshot.child("area").getValue().toString();

                                if (currentCircle != null) currentCircle.remove();

                                LatLng position = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

                                CircleOptions circleOptions = new CircleOptions().center(position) //원점
                                        .radius(Integer.parseInt(area) * 1000)      //반지름 단위 : m
                                        .strokeWidth(0f)  //선너비 0f : 선없음
                                        .fillColor(Color.parseColor("#880000ff")); //배경색

                                currentCircle = mMap.addCircle(circleOptions);
                            } catch (Exception e) {
                                Toast.makeText(User_LocationActivity.this, "안전구역은 미설정 상태입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(User_LocationActivity.this, "보호자를 등록해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 권한 처리 + 요청 결과는 onRequestPermissionResult에서 수신
        // 위치 권한 동의 여부 확인
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(User_LocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(User_LocationActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int hasBackgroundLocationPermission = ContextCompat.checkSelfPermission(User_LocationActivity.this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasBackgroundLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 권한이 있을 경우 + 6.0 이하 버전은 권한이 자동 동의
            startLocationUpdates(); // 위치 업데이트 시작

        } else {  // 권한이 없을 경우

            // 권한 거부된 적이 있을 때
            if (ActivityCompat.shouldShowRequestPermissionRationale(User_LocationActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 권한의 필요성 설명
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // 권한 요청
                        ActivityCompat.requestPermissions(User_LocationActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            } else { // 권한 거부된 적이 없을 때
                // 권한 요청
                ActivityCompat.requestPermissions(User_LocationActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "onMapClick :");
            }
        });

    }


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());

                SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분ss초");

                Calendar time = Calendar.getInstance();

                String format_time = format.format(time.getTime());

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Users");
                DatabaseReference mReference = ref.child("user").child(uid).child("gps").child(format_time);

                Map<Object, String> his = new HashMap<>();
                his.put("latitude", ""+location.getLatitude());
                his.put("longitude", ""+location.getLongitude());
                his.put("address", ""+getCurrentAddress(currentPosition));
                his.put("time", ""+format_time);

                mReference.setValue(his);

                ref.child("user").child(uid).child("myProtector").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    String myPro;
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        myPro = task.getResult().getValue().toString();
                        if (!myPro.equals("none")) {
                            ref.child("protector").child(myPro).child("safeZone").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                double latit, longi, area, distance;
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    try {
                                        latit = Double.parseDouble(task.getResult().child("latitude").getValue().toString());
                                        longi = Double.parseDouble(task.getResult().child("longitude").getValue().toString());
                                        area = Double.parseDouble(task.getResult().child("area").getValue().toString());

                                        double rad = 6372.8;

                                        double safeLat = Math.toRadians(latit - location.getLatitude());
                                        double safeLon = Math.toRadians(longi - location.getLongitude());

                                        double a = sin(safeLat / 2) * sin(safeLat / 2) + sin(safeLon / 2) * sin(safeLon / 2)
                                                * cos(Math.toRadians(location.getLatitude())) * cos(Math.toRadians(latit));

                                        double b = 2 * asin(sqrt(a));
                                        distance = rad * b;

                                        if (distance > area) {
                                            Toast.makeText(getApplicationContext(), "안전구역을 벗어났습니다.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "안전구역 이내입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(User_LocationActivity.this, "안전구역은 미설정 상태입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(User_LocationActivity.this, "보호자를 등록해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                address.setText(getCurrentAddress(currentPosition));    // 주소 반환
                String markerTitle = "현위치";
                String markerSnippet = getCurrentAddress(currentPosition);

                Log.d(TAG, "onLocationResult : " + markerSnippet);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 14));

                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);
                mCurrentLocation = location;
            }
        }
    };

    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {
            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {
                Log.d(TAG, "startLocationUpdates : 권한이 없음");
                return;
            }
            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            if (checkPermission()) mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        if (checkPermission()) {
            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            if (mMap!=null) mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public String getCurrentAddress(LatLng latlng) {
        // GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude,1);
        } catch (IOException ioException) {     // 네트워크 문제
            Toast.makeText(this, "인터넷 연결 없음", Toast.LENGTH_LONG).show();
            return "인터넷 연결 없음";
        } catch (IllegalArgumentException illegalArgumentException) { // 부정한 인수
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }
        if (addresses == null || addresses.size() == 0) {   // 주소가 없을 때
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        //if (currentMarker != null) currentMarker.remove(); // 이전 마커 지우기
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        MyItem locateItem = new MyItem(currentLatLng);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);  // 카메라를 지정한 경도, 위도로 이동
        mMap.animateCamera(CameraUpdateFactory.zoomTo(9));

        mClusterManager.addItem(locateItem);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
    }

    // 권한 처리를 위한 메소드
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }
        return false;
    }

    // ActivityCompat.requestPermissions를 사용한 권한 요청의 결과를 리턴받는 메소드
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 권한 개수만큼 수신되었다면
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults); // 대기조
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            boolean check_result = true;

            // 모든 권한 동의 여부 확인
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if (check_result) {
                // 권한 동의 시, 위치 업데이트
                startLocationUpdates();
            } else { // 거부된 권한이 있는 경우

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[2])) {

                    // 사용자가 스낵바만 거부만 선택한 경우 -> 앱 재시작 해결
                    Snackbar.make(mLayout, "권한이 거부되었습니다. 앱을 다시 실행하여 권한을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();

                } else {
                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우 -> 설정에서 권한 허용
                    Snackbar.make(mLayout, "권한이 거부되었습니다. 설정(앱 정보)에서 권한을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }
            }
        }
    }

    // GPS 활성화를 위한 메소드
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(User_LocationActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자의 GPS 활성 유무 확인
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");
                        needRequest = true;
                        return;
                    }
                }
                break;
        }
    }

    public class MyItem implements ClusterItem {
        private LatLng currentLatLng;

        public MyItem(LatLng currentLatLng) {
            this.currentLatLng = currentLatLng;
        }

        @NonNull
        @Override
        public LatLng getPosition() {
            return currentLatLng;
        }

        @Nullable
        @Override
        public String getTitle() {
            return null;
        }

        @Nullable
        @Override
        public String getSnippet() {
            return null;
        }
    }


}
