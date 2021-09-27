package com.example.logintext;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class BootCompletedReceiver extends BroadcastReceiver {

    public AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        // 자정마다 어떤 행위를 처리하기 위해 Date 값 생성
        GregorianCalendar twopm = new GregorianCalendar();

        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        twopm.set(GregorianCalendar.HOUR_OF_DAY, 24);
        twopm.set(GregorianCalendar.MINUTE, 0);
        twopm.set(GregorianCalendar.SECOND, 0);
        twopm.set(GregorianCalendar.MILLISECOND, 0);
        if(twopm.before(new GregorianCalendar())){
            twopm.add(GregorianCalendar.DAY_OF_MONTH, 1);
        }

        // PendingIntent를 통해서 setRepeating 해주는데 반복시간은 24시간마다 실행한다.
        PendingIntent alarmReceiver;
        alarmReceiver = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReceiver.class),0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, twopm.getTimeInMillis(), 1000*60*60*24, alarmReceiver);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent i = new Intent(context, UndeadService.class);
            context.startForegroundService(i);
        } else {
            Intent i = new Intent(context, UndeadService.class);
            context.startService(i);
        }


    }
}
