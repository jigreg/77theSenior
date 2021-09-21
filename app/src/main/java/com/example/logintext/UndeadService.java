package com.example.logintext;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.logintext.user.User_LocationActivity;
import com.example.logintext.user.User_MainActivity;
import com.example.logintext.user.User_TestWalkActivity;
import com.example.logintext.user.User_WalkActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UndeadService extends Service implements SensorEventListener {

    private MyBinder mMyBinder = new MyBinder();

    public class MyBinder extends Binder {
        public UndeadService getService() {
            return UndeadService.this;
        }
    }

    public static Context context_main;

    private StepCallBack callBack;

    public static Intent serviceIntent = null;

    private FirebaseDatabase database;
    private DatabaseReference mReference, today_reference;
    private FirebaseUser user;

    private String uid, format_time;

    private Calendar time;

    private SimpleDateFormat format;

    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    private Sensor stepCountSensor;

//    private RemoteViews remoteViews;
//
//    private NotificationCompat.Builder builder;

    public static int mStepDetector;

    public static final String CHANNEL_ID = "WalkService_Channel";
    public static final String CHANNEL_NAME = "WalkService_Channel";

    public void setCallBack(StepCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepDetectorSensor != null) {
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(stepCountSensor != null) {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        serviceIntent = intent;

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepDetectorSensor != null) {
            sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            Toast.makeText(this, "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(stepCountSensor != null) {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
//            Toast.makeText(this, "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }

//        Intent notificationIntent = new Intent(this, User_WalkActivity.class);
        Intent notificationIntent = new Intent(this, User_TestWalkActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);

        NotificationCompat.Builder builder;
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);

            builder.setSmallIcon(R.mipmap.myicon)
                    .setContent(remoteViews)
                    .setContentIntent(pendingIntent);

            startForeground(1, builder.build());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMyBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        context_main = this;
        unRegistManager();
        if (callBack != null) callBack.onUnbindService();
        return super.onUnbind(intent);
    }

    public void unRegistManager() {
        try {
            sensorManager.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        format = new SimpleDateFormat ( "yyyybMbd");
        time = Calendar.getInstance();

        format_time = format.format(time.getTime());

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        today_reference = database.getReference("Users").child("user").child(uid);
        mReference = today_reference.child("walk").child("date").child(format_time);

        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0f) {
                mStepDetector += event.values[0];

//                remoteViews.setTextViewText(R.id.step_count, mStepDetector+"");
//                builder.setContent(remoteViews);

                Toast.makeText(getApplicationContext(), mStepDetector+"걸음", Toast.LENGTH_SHORT).show();

                Map<String, Object> his = new HashMap<>();
                his.put("walking", mStepDetector);

                Map<String, Object> towalk = new HashMap<>();
                towalk.put("today_walking", mStepDetector);

                mReference.updateChildren(his);
                today_reference.updateChildren(towalk);


                if (callBack != null) callBack.onStepCallback(mStepDetector);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
