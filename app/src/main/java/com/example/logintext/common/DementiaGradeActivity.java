package com.example.logintext.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.example.logintext.user.User_MainActivity;
import com.example.logintext.user.User_SettingActivity;

public class DementiaGradeActivity extends AppCompatActivity {

    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dementia_grade);

        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DementiaGradeActivity.this, User_SettingActivity.class));
                finish();
            }
        });
    }
}
