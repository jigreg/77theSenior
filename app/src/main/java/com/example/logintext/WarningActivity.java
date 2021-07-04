package com.example.logintext;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class WarningActivity extends AppCompatActivity {
    Button bt;
    NotificationManager manager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showNoti();
            }
        });
    }

    public void showNoti(){
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ // 오레오 이상 버전 일 경우
            manager.createNotificationChannel( new NotificationChannel(CHANNEL_ID,
                    CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT) );
            builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        }else{
            builder = new NotificationCompat.Builder(this);
        }
        builder.setContentTitle("77맞은 노인"); // 알림창 제목
        builder.setContentText("사용자님께서 안전구역을 벗어났습니다.");   // 알림창 내용
        builder.setSmallIcon(R.mipmap.naver1);    // 알림창 아이콘
        Notification notification = builder.build();
        manager.notify(1,notification);
    }
}

