package com.example.logintext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseActivity extends AppCompatActivity {
    public static Context context_choose;
    ImageButton back;
    Button protector, user;
    static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        context_choose = this;

        protector = (Button)findViewById(R.id.protector);
        user = (Button)findViewById(R.id.user);
        back = (ImageButton)findViewById(R.id.before12);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseActivity.this, LoginActivity.class));
                finish();
            }
        });
        protector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "protector";
                Toast.makeText(getApplicationContext(),"보호자를 선택하셨습니다.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ChooseActivity.this, RegisterActivity.class));
                finish();
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "user";
                Toast.makeText(getApplicationContext(),"사용자를 선택하셨습니다.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ChooseActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }
}