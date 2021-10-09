package com.example.logintext;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.logintext.user.User_MainActivity;


public class PushAlarmReceiver extends BroadcastReceiver {

    public PushAlarmReceiver(){ }

    private NotificationManager manager;
    private NotificationCompat.Builder builder;


    public static final String CHANNEL_ID = "Push_Alram";
    public static final String CHANNEL_NAME = "Push_Alram_Service";

    @Override
    public void onReceive(Context context, Intent intent) {
        builder = null;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        Intent intent2 = new Intent(context, User_MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.push_notification);

        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);

            builder.setSmallIcon(R.mipmap.myicon)
                    .setContent(remoteViews)
                    .setContentIntent(pendingIntent);

            Notification notification = builder.build();
            manager.notify(2,notification);
        }
    }
}