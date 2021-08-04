package com.example.logintext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class UserSettingActivity extends AppCompatActivity {
    ImageButton before8;
    Button proCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_set);

        before8 = (ImageButton)findViewById(R.id.before8);
        proCode = (Button)findViewById(R.id.button12);

        before8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSettingActivity.this, UserMainActivity.class));
                finish();
            }
        });

        proCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSettingActivity.this, ProtectorRegisterCodeActivity.class));
                finish();
            }
        });
    }
}