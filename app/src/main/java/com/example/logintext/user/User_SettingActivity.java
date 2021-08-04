package com.example.logintext.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;

public class User_SettingActivity extends AppCompatActivity {
    private ImageButton back;
    private Button proCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_set);

        back = (ImageButton)findViewById(R.id.back);
        proCode = (Button)findViewById(R.id.button12);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_SettingActivity.this, User_MainActivity.class));
                finish();
            }
        });

        proCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_SettingActivity.this, User_RegisterCodeActivity.class));
                finish();
            }
        });
    }
}