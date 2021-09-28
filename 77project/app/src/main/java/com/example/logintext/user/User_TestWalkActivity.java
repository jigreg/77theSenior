package com.example.logintext.user;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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
import java.util.HashMap;
import java.util.Map;


public class User_TestWalkActivity extends AppCompatActivity {

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
    private DatabaseReference mReference, today_reference;
    private FirebaseUser user;

    private int mStepDetector = ((UndeadService) UndeadService.context_main).mStepDetector;

    private UndeadService stepService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Toast.makeText(getApplicationContext(), "예쓰바인딩", Toast.LENGTH_SHORT).show();
            UndeadService.MyBinder mb  = (UndeadService.MyBinder) service;
            stepService = mb.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            Toast.makeText(getApplicationContext(), "디스바인딩", Toast.LENGTH_SHORT).show();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_walk);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        stepService = new UndeadService();

        Intent foregroundServiceIntent = new Intent(User_TestWalkActivity.this, UndeadService.class);
//        startService(foregroundServiceIntent);
        bindService(foregroundServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

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

        cal = Calendar.getInstance();

        date.setText(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE));

        circleProgressBar.setProgressFormatter((progress, max) -> {
            final String DEFAULT_PATTERN = "목표 %d 걸음 -> %d 걸음";
            return String.format(DEFAULT_PATTERN, max, progress); });
        circleProgressBar.setMax(100);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_TestWalkActivity.this, User_MainActivity.class));
                finish();
            }
        });

        calen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_TestWalkActivity.this, User_CalendarActivity.class));
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

        // 걸음 데이터 데이터베이스에 업로드
        format = new SimpleDateFormat ( "yyyybMbd");
        time = Calendar.getInstance();

        format_time = format.format(time.getTime());

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        today_reference = database.getReference("Users").child("user").child(uid);
        mReference = today_reference.child("walk").child("date").child(format_time);

        Map<String, Object> his = new HashMap<>();
        his.put("walking", mStepDetector);
        his.put("time", format_time);

        Map<String, Object> towalk = new HashMap<>();
        towalk.put("today_walking", mStepDetector);

        mReference.setValue(his);
        today_reference.updateChildren(towalk);

        //걸음 데이터 불러오기
        today_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                walk = (String.valueOf(dataSnapshot.child("walk").child("date").child(format_time).child("walking").getValue()));

                countWalk.setText(walk + "걸음");
                progressBar.setProgress(Integer.valueOf(walk));
                circleProgressBar.setProgress(Integer.valueOf(walk));

                try {
                    distance = (float) Integer.valueOf(walk);
                    dis.setText(String.format("%.2f Km ", (distance * 0.0007f)));
                    calorie.setText(String.format("%.2f cal", (distance * 0.0374f)));
                } catch (Exception e) {
                }

                if  (walk.equals("null")) {
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

//    public static void resetAlarm(Context context){
//        AlarmManager resetAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        Intent resetIntent = new Intent(context, Alarmservice.class);
//        PendingIntent resetSender = PendingIntent.getBroadcast(context, 0, resetIntent, 0); // 자정 시간
//        Calendar resetCal = Calendar.getInstance(); resetCal.setTimeInMillis(System.currentTimeMillis());
//        resetCal.set(Calendar.HOUR_OF_DAY, 0); resetCal.set(Calendar.MINUTE,0); resetCal.set(Calendar.SECOND, 0);
//
//        // 다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌.
//        resetAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, resetCal.getTimeInMillis() +
//                AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, resetSender);
//        SimpleDateFormat format = new SimpleDateFormat("MM/dd kk:mm:ss");
//        String setResetTime = format.format(new Date(resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY));
//        Log.d("resetAlarm", "ResetHour : " + setResetTime);
//    }


    // calender
    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    // Date Picker에서 선택한 날짜를 TextView에 설정
                    date.setText(String.format("%d-%d-%d", yy, mm + 1, dd));
                }
            };
}
