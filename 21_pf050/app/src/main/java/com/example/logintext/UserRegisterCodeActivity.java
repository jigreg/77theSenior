package com.example.logintext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterCodeActivity extends AppCompatActivity {
    ImageButton before2;
    EditText edit;
    Button addUser;
    CheckBox cb;

    String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_code);

        before2 = (ImageButton)findViewById(R.id.before10);
        edit = (EditText) findViewById(R.id.code);
        addUser = (Button) findViewById(R.id.enterCode);
        cb = (CheckBox) findViewById(R.id.checkbox5);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()) {
                    edit.setClickable(true);
                    edit.setFocusable(true);
                    edit.setFocusableInTouchMode(true);
                    edit.requestFocus();
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked() == false) {
                    Toast.makeText(getApplicationContext(), "체크박스를 활성화해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        before2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRegisterCodeActivity.this, ProtectorMainActivity.class));
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Users");
                ref.child("user").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        for (DataSnapshot messageData : task.getResult().getChildren()) {
                            String msg = messageData.child("uid").getValue().toString();
                            code = msg.substring(0,6);

                            String com = edit.getText().toString();

                            if (com.equals(code)) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = user.getUid();
                                DatabaseReference myUserReference = ref.child("protector").child(uid);

                                Map<String, Object> userReg = new HashMap<>();
                                userReg.put("myUser", msg);
                                myUserReference.updateChildren(userReg);

                                DatabaseReference myProtectorReference = ref.child("user").child(msg);
                                Map<String, Object> proReg = new HashMap<>();
                                proReg.put("myProtector", uid);
                                myProtectorReference.updateChildren(proReg);

                                Toast.makeText(getApplicationContext(), "사용자 등록이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UserRegisterCodeActivity.this, ProtectorMainActivity.class));
                                finish();

                                return;
                            }
                        }
                    }
                });
            }
        });
    }
}