package com.example.logintext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.logintext.user.User_WalkActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Alarmservice extends BroadcastReceiver {
    private int mStepDetector = User_WalkActivity.mStepDetector;
    String uid;

    @Override
    public void onReceive(Context context, Intent intent) {

        SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월dd일 ");

        Calendar time = Calendar.getInstance();

        String format_time = format.format(time.getTime());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        DatabaseReference mReference = ref.child("user").child(uid).child("walk").child(format_time);

        Map<String, Object> his = new HashMap<>();
        his.put("walk", mStepDetector);
        his.put("time", ""+format_time);

        mReference.updateChildren(his);
    }
}
