package com.example.logintext;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.DatePicker;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class WalkActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    TextView wk3, dis, calo, us1, count1;
    static int mStepDetector;
    ImageButton before, calen;
    private Sensor stepCountSensor;
    TextView tvStepCount, tv;
    float dis1;
    ProgressBar pb;
    CircleProgressBar cp;
    String  uid, walkin, name, walkk;
    Handler handler;
    int cnt = 0;
    DatePicker datePicker;
    public static int context_walk;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk);
        tv = findViewById(R.id.date);
        before = (ImageButton) findViewById(R.id.before7);
        calen = (ImageButton) findViewById(R.id.calendar3);
        Calendar cal = Calendar.getInstance();
        tv.setText(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE));
        pb = (ProgressBar) findViewById(R.id.progressBar);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        wk3 = (TextView) findViewById(R.id.walk3);
        dis = (TextView) findViewById(R.id.distance);
        calo = (TextView) findViewById(R.id.calorie);
        us1 = (TextView)findViewById(R.id.us1);
        count1 = (TextView) findViewById(R.id.count1);
        datePicker = (DatePicker)findViewById(R.id.vDatePicker);

        context_walk = mStepDetector;

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        cp = (CircleProgressBar) findViewById(R.id.circleBar);
        cp.setProgressFormatter((progress, max) -> {
            final String DEFAULT_PATTERN = "목표 %d 걸음 -> %d 걸음";
            return String.format(DEFAULT_PATTERN, max, progress); });
        cp.setMax(100);

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WalkActivity.this, UserMainActivity.class));
            }
        });

//        calen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(WalkActivity.this, Calendar.class));
//            }
//        });

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        tv.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                });

        // DETECTOR
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepDetectorSensor == null) {
            Toast.makeText(this, "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }


        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }

        calen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일 ");

                Calendar time = Calendar.getInstance();

                String format_time = format.format(time.getTime());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Users");
                DatabaseReference mReference = ref.child("user").child(uid).child("walk").child(format_time);

                Map<String, Object> his = new HashMap<>();
                his.put("walked", mStepDetector);
                his.put("time", ""+format_time);

                mReference.updateChildren(his);
            }
        });

        SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일 ");

        Calendar time = Calendar.getInstance();

        String format_time = format.format(time.getTime());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users").child(uid).child("user").child("walk").child(format_time);

        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("walk").getValue().toString().equals("0")) {
                    walkk = dataSnapshot.child("walk").child(format_time).getValue().toString();
                } else {
                    walkk = "0";
                }
                name = dataSnapshot.child("name").getValue().toString();
                us1.setText("닉네임 : "+ name );
                count1.setText("걸음 수 : " + walkk);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"onCancelled", Toast.LENGTH_SHORT);
            }
        });

        return;

    }
    public void onStartForegroundService(View view) {
        Intent intent = new Intent(this, WalkActivity.class);
        intent.setAction("startForeground");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    public static void resetAlarm(Context context){
        AlarmManager resetAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent resetIntent = new Intent(context, Alarmservice.class);
        PendingIntent resetSender = PendingIntent.getBroadcast(context, 0, resetIntent, 0); // 자정 시간
        Calendar resetCal = Calendar.getInstance(); resetCal.setTimeInMillis(System.currentTimeMillis());
        resetCal.set(Calendar.HOUR_OF_DAY, 0); resetCal.set(Calendar.MINUTE,0); resetCal.set(Calendar.SECOND, 0);

        // 다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌.
        resetAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, resetCal.getTimeInMillis() +
                AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, resetSender);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd kk:mm:ss");
        String setResetTime = format.format(new Date(resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY));
        Log.d("resetAlarm", "ResetHour : " + setResetTime);
    }


    // calender
    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    // Date Picker에서 선택한 날짜를 TextView에 설정
                    tv.setText(String.format("%d-%d-%d", yy, mm + 1, dd));
                }
            };


    @Override
    protected void onResume () {
        super.onResume();
        sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause () {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged (SensorEvent event){
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0f) {
                mStepDetector += event.values[0];
                wk3.setText(String.valueOf(mStepDetector) + "걸음");
                pb.setProgress(mStepDetector);
                cp.setProgress(mStepDetector);
                try {
                    dis1 = (float)mStepDetector;
                    dis.setText(String.format("%.2f Km ", (dis1 * 0.0007f)));
                    calo.setText(String.format("%.2f cal", (dis1 * 0.0374f)));
                } catch (Exception e) {
                }
            }
        }
//        else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//            tvStepCount.setText("총 걸음수 : " + String.valueOf(event.values[0]));
//        }
    }

    @Override
    public void onAccuracyChanged (Sensor sensor,int accuracy){

    }
}