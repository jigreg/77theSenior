package com.example.logintext.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class User_PushAlarmActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference, refer;
    private FirebaseUser user;

    private String uid;

    private Switch all, daily, rank, safe, market;

    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_alarm_set);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
        mReference = mDatabase.getReference("Users");
        try {
            refer = mReference.child("user").child(uid).child("alarm");
        } catch (NullPointerException e) {
            Map<String, Object> pushMap = new HashMap<>();
            pushMap.put("daily", "y");
            pushMap.put("safeZone", "y");
            pushMap.put("ranking", "y");
            pushMap.put("marketing", "y");

            refer.updateChildren(pushMap);
        }

        all = (Switch) findViewById(R.id.all_set);
        daily = (Switch) findViewById(R.id.msg_set);
        rank = (Switch) findViewById(R.id.rank_set);
        safe = (Switch) findViewById(R.id.safe_set);
        market = (Switch) findViewById(R.id.market_set);

        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_PushAlarmActivity.this, User_SettingActivity.class));
                finish();
            }
        });

        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    daily.setChecked(true);
                    rank.setChecked(true);
                    safe.setChecked(true);
                    market.setChecked(true);

                    Map<String, Object> pushMap = new HashMap<>();
                    pushMap.put("daily", "y");
                    pushMap.put("safeZone", "y");
                    pushMap.put("ranking", "y");
                    pushMap.put("marketing", "y");

                    refer.updateChildren(pushMap);
                } else {
                    daily.setChecked(false);
                    rank.setChecked(false);
                    safe.setChecked(false);
                    market.setChecked(false);

                    Map<String, Object> pushMap = new HashMap<>();
                    pushMap.put("daily", "n");
                    pushMap.put("safeZone", "n");
                    pushMap.put("ranking", "n");
                    pushMap.put("marketing", "n");

                    refer.updateChildren(pushMap);
                }
            }
        });

        daily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map<String, Object> daily_switch = new HashMap<>();
                if (isChecked) {
                    daily_switch.put("daily", "y");
                } else {
                    daily_switch.put("daily", "n");
                }
                refer.updateChildren(daily_switch);
            }
        });

        rank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map<String, Object> rank_switch = new HashMap<>();
                if (isChecked) {
                    rank_switch.put("ranking", "y");
                } else {
                    rank_switch.put("ranking", "n");
                }
                refer.updateChildren(rank_switch);
            }
        });

        safe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map<String, Object> safe_switch = new HashMap<>();
                if (isChecked) {
                    safe_switch.put("safeZone", "y");
                } else {
                    safe_switch.put("safeZone", "n");
                }
                refer.updateChildren(safe_switch);
            }
        });

        market.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map<String, Object> market_switch = new HashMap<>();
                if (isChecked) {
                    market_switch.put("marketing", "y");
                } else {
                    market_switch.put("marketing", "n");
                }
                refer.updateChildren(market_switch);
            }
        });

        refer.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String daily_set = task.getResult().child("daily").getValue().toString();
                String rank_set = task.getResult().child("ranking").getValue().toString();
                String safe_set = task.getResult().child("safeZone").getValue().toString();
                String market_set = task.getResult().child("marketing").getValue().toString();

                if (daily_set.equals("y")) daily.setChecked(true);
                if (rank_set.equals("y")) rank.setChecked(true);
                if (safe_set.equals("y")) safe.setChecked(true);
                if (market_set.equals("y")) market.setChecked(true);

                if (daily.isChecked() && rank.isChecked() && safe.isChecked()
                        && market.isChecked()) all.setChecked(true);
            }
        });

    }
}


