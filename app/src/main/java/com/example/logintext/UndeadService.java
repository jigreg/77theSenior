package com.example.logintext;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.logintext.user.User_LocationActivity;

import java.util.Calendar;

public class UndeadService extends Service {

    public static Intent serviceIntent = null;

    private BackgroundTask task;
    private int value = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;

        task = new BackgroundTask();
        task.execute();

        initializeNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    public void initializeNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.myicon)
                .setContentTitle(null)
                .setContentText(null)
                .setOngoing(true);


        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("설정을 보려면 누르세요.")
                .setBigContentTitle(null)
                .setSummaryText("서비스 동작중");

        builder.setStyle(style)
                .setWhen(0)
                .setShowWhen(false);

        Intent notificationIntent = new Intent(this, User_LocationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1", "undead_service", NotificationManager.IMPORTANCE_NONE));
        }

        Notification notification = builder.build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, BootCompletedReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

        task.cancel(true);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, BootCompletedReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    class BackgroundTask extends AsyncTask<Integer, String, Integer> {

        String result = "";

        @Override
        protected Integer doInBackground(Integer... integers) {
            while(isCancelled() == false) {
                try {
                    Toast.makeText(getApplicationContext(),value+"번째 실행중", Toast.LENGTH_SHORT).show();
                    Thread.sleep(1000);
                    value++;
                } catch (InterruptedException e) {}
            }

            return value;
        }
    }
}
