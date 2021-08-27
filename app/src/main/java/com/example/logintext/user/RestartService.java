//package com.example.logintext.user;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.IBinder;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//
//import com.example.logintext.R;
//
//public class RestartService extends Service {
//    public static Context mContext;
//
//    public RestartService() {
//    }
//
//    @Override
//    public void onCreate()
//    {
//        mContext = this;
//        super.onCreate();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(getApplication(), "Stop Service", Toast.LENGTH_SHORT);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
//                .setSmallIcon(R.mipmap.myicon)
//                .setContentTitle("77 맞은 어르신")
//                .setContentText("테스트");
//        Intent notificationIntent = new Intent(this, User_WalkActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        builder.setContentIntent(pendingIntent);
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_NONE));
//        }
//
//        Notification notification = builder.build();
//        startForeground(9, notification);
//
//        Intent in = new Intent(this, RealService.class);
//        startService(in);
//
//        stopForeground(true);
//        stopSelf();
//
//        return START_NOT_STICKY;
//        // START_NOT_STICKY -> 서비스 중단 시, 재생성X
//        // START_STICKY -> 서비스 중단 시, 재시작O, 인텐트 전달X -> 무기한 동작해야할 때 적합
//        // START_REDELIVER_INTENT -> START_STICKY와 다르게, 인텐트와 함께 서비스를 재시작
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        return super.onUnbind(intent);
//    }
//
//}
