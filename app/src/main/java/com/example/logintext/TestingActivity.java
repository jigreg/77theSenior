package com.example.logintext;


import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.logintext.Alarmservice;
import com.example.logintext.R;
import com.example.logintext.UndeadService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TestingActivity extends AppCompatActivity {

    private UndeadService stepService;
    boolean isService = false;

    private TextView textCount, statusService;
    private Button startBtn, endBtn;
    private Intent intent;


    private StepCallBack stepCallBack = new StepCallBack() {
        @Override
        public void onStepCallback(int step) {
            textCount.setText("" + step);
        }

        @Override
        public void onUnbindService() {
            isService = false;
            statusService.setText("해체됨");
            Toast.makeText(getApplicationContext(), "디스바인딩", Toast.LENGTH_SHORT).show();
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(getApplicationContext(), "예쓰바인딩", Toast.LENGTH_SHORT).show();
            UndeadService.MyBinder mb  = (UndeadService.MyBinder) service;
            stepService = mb.getService();
            stepService.setCallBack(stepCallBack);
            isService = true;
            statusService.setText("연결됨");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isService = false;
            statusService.setText("해체됨");
//            Toast.makeText(getApplicationContext(), "디스바인딩", Toast.LENGTH_SHORT).show();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        stepService = new UndeadService();
        startBtn = (Button) findViewById(R.id.startBtn);
        endBtn = (Button) findViewById(R.id.endBtn);
        textCount = (TextView) findViewById(R.id.textCount);
        statusService = (TextView) findViewById(R.id.textStatusService);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(TestingActivity.this, UndeadService.class);
                startService(intent);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stopService(intent);
                    unbindService(serviceConnection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    @Override 우리는 메인에서 종료시킬거 ㅇㅇ
//    protected void onStop() {
//        super.onStop();
//        unbindService(serviceConnection);
//    }
}