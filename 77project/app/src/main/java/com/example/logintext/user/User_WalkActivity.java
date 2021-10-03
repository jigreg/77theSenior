package com.example.logintext.user;


import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class User_WalkActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepDetectorSensor, stepCountSensor;

    private ProgressBar progressBar;
    private CircleProgressBar circleProgressBar;

    private TextView countWalk, dis, calorie, userNickName, count, date;
    private ImageButton back, calen;
    private DatePicker datePicker;
    private Calendar cal, time;

    private String uid, nickname, walk, format_time;
    private float distance;

    private SimpleDateFormat format;

    private FirebaseDatabase database;
    private DatabaseReference ref, mReference, nReference, today_reference;
    private FirebaseUser user;

    public static int mStepDetector;
    public static int context_walk;
    public static Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_walk);

        mContext = this;

        Intent foregroundServiceIntent = new Intent(User_WalkActivity.this, UndeadService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(foregroundServiceIntent);
        }

        back = (ImageButton) findViewById(R.id.back);
        calen = (ImageButton) findViewById(R.id.calendar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        circleProgressBar = (CircleProgressBar) findViewById(R.id.circleBar);

        date = (TextView) findViewById(R.id.date);
        countWalk = (TextView) findViewById(R.id.walk);
        dis = (TextView) findViewById(R.id.distance);
        calorie = (TextView) findViewById(R.id.calorie);
        userNickName = (TextView)findViewById(R.id.userNickName);
        count = (TextView) findViewById(R.id.count);

        datePicker = (DatePicker)findViewById(R.id.datePicker);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        context_walk = mStepDetector;

        cal = Calendar.getInstance();

        date.setText(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE));

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        circleProgressBar.setProgressFormatter((progress, max) -> {
            final String DEFAULT_PATTERN = "목표 %d 걸음 -> %d 걸음";
            return String.format(DEFAULT_PATTERN, max, progress); });
        circleProgressBar.setMax(100);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_WalkActivity.this, User_MainActivity.class));
                finish();
            }
        });

        calen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_WalkActivity.this, User_CalendarActivity.class));
                finish();
            }
        });

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
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

        //걸음 데이터 데이터베이스에 업로드
        format = new SimpleDateFormat ( "yyyybMbd");
        time = Calendar.getInstance();

        format_time = format.format(time.getTime());

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");
        mReference = ref.child("user").child(uid).child("walk").child("date").child(format_time);
        today_reference = ref.child("user").child(uid);

        Map<String, Object> his = new HashMap<>();
        his.put("walking", mStepDetector);
        his.put("time", format_time);

        Map<String, Object> towalk = new HashMap<>();
        towalk.put("today_walking", mStepDetector);

        mReference.setValue(his);
        today_reference.updateChildren(towalk);

        //걸음 데이터 불러오기
        nReference = ref.child("user").child(uid);
        nReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                walk = (String.valueOf(dataSnapshot.child("walk").child("date").child(format_time).child("walking").getValue()));
                if(walk.equals("null")) {
                    count.setText("오늘도 걸어봅시다!");
                } else {
                    count.setText("걸음 수 : " + walk);
                }
                nickname = dataSnapshot.child("name").getValue().toString();
                userNickName.setText("닉네임 : "+ nickname );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"onCancelled", Toast.LENGTH_SHORT);
            }
        });

        return;
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
                    date.setText(String.format("%d-%d-%d", yy, mm + 1, dd));
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
                countWalk.setText(String.valueOf(mStepDetector) + "걸음");
                progressBar.setProgress(mStepDetector);
                circleProgressBar.setProgress(mStepDetector);

                Toast.makeText(getApplicationContext(), mStepDetector+"걸음",Toast.LENGTH_SHORT).show();

                Map<String, Object> his = new HashMap<>();
                his.put("walking", mStepDetector);

                Map<String, Object> towalk = new HashMap<>();
                towalk.put("today_walking", mStepDetector);

                mReference.updateChildren(his);
                today_reference.updateChildren(towalk);

                try {
                    distance = (float) mStepDetector;
                    dis.setText(String.format("%.2f Km ", (distance * 0.0007f)));
                    calorie.setText(String.format("%.2f cal", (distance * 0.0374f)));
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged (Sensor sensor,int accuracy){

    }
}