package com.example.logintext;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;


public class AlarmReceiver extends BroadcastReceiver {

    private FirebaseDatabase mDatabase;
    private DatabaseReference today_reference;
    private FirebaseUser user;
    public AlarmReceiver(){ }
    private String uid;

    @Override
    public void onReceive(Context context, Intent intent) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
        today_reference = mDatabase.getReference("Users").child("user").child(uid);

        Map<String, Object> reset = new HashMap<>();
        reset.put("today_walking",0);
        reset.put("today_training", 0);

        today_reference.updateChildren(reset);
        ((UndeadService)UndeadService.context_main).mStepDetector = 0;
    }

}