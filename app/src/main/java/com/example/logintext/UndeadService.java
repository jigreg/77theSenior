package com.example.logintext;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.logintext.user.User_LocationActivity;
import com.example.logintext.user.User_WalkActivity;

public class UndeadService extends Service {

    public static final String CHANNEL_ID = "WalkService_Channel";
    public static final String CHANNEL_NAME = "WalkService_Channel";

    @Override
    public void onCreate() {
        Toast.makeText(this, "포그라운드 고고", Toast.LENGTH_SHORT).show();

        Intent notificationIntent = new Intent(this, User_WalkActivity.class);
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

        return null;
    }
}
