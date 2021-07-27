package com.example.logintext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class UserMainActivity extends AppCompatActivity {

    Button logout, walk, locate, setting, ranking, calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        logout = (Button) findViewById(R.id.logout);
        walk = (Button) findViewById(R.id.walk);
        locate = (Button) findViewById(R.id.locate);
        ranking = (Button) findViewById(R.id.ranking);
        setting = (Button) findViewById(R.id.set2);
        calendar = (Button) findViewById(R.id.calendar2);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "로그아웃 합니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserMainActivity.this, LoginActivity.class));
                finish();
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, WalkActivity.class));
                finish();
            }
        });

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, UserLocationActivity.class));
                finish();
            }
        });

        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, RankActivity.class));
                finish();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, UserSettingActivity.class));
                finish();
            }
        });
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, CalendarActivity.class));
                finish();
            }
        });
    }
}