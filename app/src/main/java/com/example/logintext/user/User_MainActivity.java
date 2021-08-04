package com.example.logintext.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.example.logintext.common.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class User_MainActivity extends AppCompatActivity {

    private Button logout, walk, training, locate, setting, ranking, calendar;

    private FirebaseAuth mAuth;

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
}