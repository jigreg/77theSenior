package com.example.logintext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent i = new Intent(context, UndeadService.class);
            context.startForegroundService(i);
        } else {
            Intent i = new Intent(context, UndeadService.class);
            context.startService(i);
        }
    }
}
