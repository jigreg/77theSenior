package com.example.logintext.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.logintext.R;
import com.example.logintext.common.LoginActivity;
import com.example.logintext.common.LoginMaintainService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_MainActivity extends AppCompatActivity {

    private Button logout, walk, training, locate, setting, ranking, calendar;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        logout = (Button) findViewById(R.id.logout);
        walk = (Button) findViewById(R.id.walking);
        training = (Button)findViewById(R.id.brainTraining);
        locate = (Button) findViewById(R.id.locate);
        ranking = (Button) findViewById(R.id.ranking);
        setting = (Button) findViewById(R.id.setting);
        calendar = (Button) findViewById(R.id.calendar);

        mAuth = FirebaseAuth.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                LoginMaintainService.clearUserName(User_MainActivity.this);
                Toast.makeText(getApplicationContext(), "로그아웃 합니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(User_MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_MainActivity.this, User_WalkActivity.class));
                finish();
            }
        });

        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_MainActivity.this, User_BrainMain.class));
                finish();
            }
        });

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_MainActivity.this, User_LocationActivity.class));
                finish();
            }
        });

        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_MainActivity.this, User_WalkRankActivity.class));
                finish();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_MainActivity.this, User_SettingActivity.class));
                finish();
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_MainActivity.this, User_CalendarActivity.class));
                finish();
            }
        });
    }

//    포그라운드 기능
//    private void startForegroundService() {
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        uid = user.getUid();
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("Users");
//        DatabaseReference mReference = ref.child("user").child(uid);
//
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String walked = snapshot.child("walk").getValue().toString();
//
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
//                builder.setSmallIcon(R.mipmap.myicon);
//                builder.setContentTitle("77 맞은 어르신");
//                builder.setContentText("현재 걸음수 : "+ walked);
//
//                Intent notificationIntent = new Intent(this, );
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

}