package com.example.logintext.protector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.example.logintext.common.AppInfoActivity;
import com.example.logintext.common.PushAlramActivity;
import com.example.logintext.user.User_MainActivity;
import com.example.logintext.user.User_RegisterCodeActivity;

public class Pro_SettingActivity extends AppCompatActivity {

    private ImageButton back;
    private Button association, push_alram, dementia_grade, proCode, terms_service, personal_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_set);

        back = (ImageButton) findViewById(R.id.back);

        association = (Button) findViewById(R.id.association);
        push_alram = (Button) findViewById(R.id.push_alram);
        dementia_grade = (Button) findViewById(R.id.dementia_grade);
        proCode = (Button) findViewById(R.id.pro_register_code);
        terms_service = (Button) findViewById(R.id.terms_of_service);
        personal_info = (Button) findViewById(R.id.personal_info);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pro_SettingActivity.this, User_MainActivity.class));
                finish();
            }
        });


        association.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pro_SettingActivity.this, AppInfoActivity.class));
                finish();
            }
        });

        push_alram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pro_SettingActivity.this, PushAlramActivity.class));
                finish();
            }
        });

        dementia_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pro_SettingActivity.this, Pro_RegisterCodeActivity.class));
                finish();
            }
        });

        proCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pro_SettingActivity.this, Pro_RegisterCodeActivity.class));
                finish();
            }
        });

        terms_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout linear = (LinearLayout) inflater.inflate(R.layout.service_info, null);

                linear.setBackgroundColor(Color.parseColor("#99000000"));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
                        , LinearLayout.LayoutParams.FILL_PARENT);
                addContentView(linear, params);

                linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewManager) linear.getParent()).removeView(linear);
                    }
                });
            }
        });

        personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout linear = (LinearLayout) inflater.inflate(R.layout.personal_info, null);

                linear.setBackgroundColor(Color.parseColor("#99000000"));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
                        , LinearLayout.LayoutParams.FILL_PARENT);
                addContentView(linear, params);

                linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewManager) linear.getParent()).removeView(linear);
                    }
                });
            }
        });
    }
}