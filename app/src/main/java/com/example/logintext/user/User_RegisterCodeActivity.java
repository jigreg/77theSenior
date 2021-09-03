package com.example.logintext.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User_RegisterCodeActivity extends AppCompatActivity {
    private ImageButton back;
    private CheckBox okCheck;
    private TextView code;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_protector_code);

        back = (ImageButton)findViewById(R.id.back);
        okCheck = (CheckBox) findViewById(R.id.checkbox);
        code = (TextView) findViewById(R.id.code);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_RegisterCodeActivity.this, User_SettingActivity.class));
                finish();
            }
        });

        okCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(okCheck.isChecked()) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    code.setText(uid.substring(0, 6));
                }
            }
        });

    }
}