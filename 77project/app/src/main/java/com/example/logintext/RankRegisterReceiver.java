package com.example.logintext;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.logintext.user.User_MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class RankRegisterReceiver extends BroadcastReceiver {

    public RankRegisterReceiver(){ }

    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;

    private String uid;

    @Override
    public void onReceive(Context context, Intent intent) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference("Users").child("user").child(uid);

        ((UndeadService)UndeadService.context_main).mStepDetector = 0;

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String rank_wk = task.getResult().child("today_walking").getValue().toString();
                String rank_tr = task.getResult().child("today_training").getValue().toString();

                Map<String, Object> hashMap = new HashMap<>();

                hashMap.put("rank_walking", rank_wk);
                hashMap.put("rank_training", rank_tr);

                ref.updateChildren(hashMap);
            }
        });

        Map<String, Object> reset = new HashMap<>();

        reset.put("today_walking", 0);
        reset.put("today_training", 0);

        ref.updateChildren(reset);

        Toast.makeText(context.getApplicationContext(), "랭킹이 초기화되었습니다.", Toast.LENGTH_SHORT).show();
    }
}