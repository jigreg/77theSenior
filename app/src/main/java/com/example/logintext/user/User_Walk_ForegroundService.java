package com.example.logintext.user;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.logintext.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Walk_ForegroundService extends Service {
    private NotificationManager manager;
    private NotificationCompat.Builder builder;

    private FirebaseDatabase database;
    private DatabaseReference mReference;
    private FirebaseUser user;

    private String uid;
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Test_Channel";

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();
    }

    @Override public void onDestroy() { super.onDestroy(); }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if("startForeground".equals(intent.getAction())) {
            startForegroundService();
        }
        return  START_STICKY;
    }

    public void startForegroundService() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ // 오레오 이상 버전 일 경우
            manager.createNotificationChannel( new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT) );
            builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        }else{
            builder = new NotificationCompat.Builder(this);
        }


        builder.setContentTitle("오늘도 걸어볼까요?") // 알림창 제목
                .setContentText("0 걸음")  // 알림창 내용
                .setSmallIcon(R.mipmap.myicon);    // 알림창 아이콘

//        user = FirebaseAuth.getInstance().getCurrentUser();
//        uid = user.getUid();
//        database = FirebaseDatabase.getInstance();
//        mReference = database.getReference().child("Users").child("user").child(uid);
//
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String walk = snapshot.child("today_walking").getValue().toString();
//
//                builder.setContentTitle("오늘도 걸어볼까요?") // 알림창 제목
//                        .setContentText(walk + " 걸음")  // 알림창 내용
//                        .setSmallIcon(R.mipmap.myicon);    // 알림창 아이콘
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        Intent notificationIntent = new Intent(this, User_WalkActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        startForeground(1, builder.build());
    }
}

