package com.example.logintext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProtectorRegisterCodeActivity extends AppCompatActivity {
    ImageButton back;
    EditText edit;
    CheckBox okCheck;
    TextView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.protector_code);

        back = (ImageButton)findViewById(R.id.before11);
        edit = (EditText) findViewById(R.id.code);
        okCheck = (CheckBox) findViewById(R.id.checkbox6);
        code = (TextView) findViewById(R.id.code4);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProtectorRegisterCodeActivity.this, UserMainActivity.class));
                finish();
            }
        });

        okCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(okCheck.isChecked()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    code.setText(uid.substring(0, 6));
                }
            }
        });

    }
}