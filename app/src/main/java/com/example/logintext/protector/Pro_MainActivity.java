package com.example.logintext.protector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.example.logintext.common.LoginActivity;
import com.example.logintext.common.LoginMaintainService;
import com.example.logintext.user.User_MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Pro_MainActivity extends AppCompatActivity {

    private Button gps, userReg, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_main);

        logout = (Button) findViewById(R.id.logout);
        gps = (Button) findViewById(R.id.locationTrace);
        userReg = (Button) findViewById(R.id.userRegister);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                LoginMaintainService.clearUserName(Pro_MainActivity.this);
                Toast.makeText(getApplicationContext(), "로그아웃 합니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Pro_MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        gps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                startActivity(new Intent(Pro_MainActivity.this, Pro_LocationActivity.class));
                finish();
            }
        });

        userReg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                startActivity(new Intent(Pro_MainActivity.this, Pro_RegisterCodeActivity.class));
                finish();
            }
        });
    }
}
