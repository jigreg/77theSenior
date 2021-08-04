package com.example.logintext.protector;

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

import com.example.logintext.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Pro_RegisterCodeActivity extends AppCompatActivity {
    private ImageButton back;
    private EditText code;
    private Button addUser;
    private CheckBox cb;

    private String codeContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_user_code);

        back = (ImageButton)findViewById(R.id.back);
        code = (EditText) findViewById(R.id.code);
        addUser = (Button) findViewById(R.id.complete);
        cb = (CheckBox) findViewById(R.id.checkbox);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()) {
                    code.setClickable(true);
                    code.setFocusable(true);
                    code.setFocusableInTouchMode(true);
                    code.requestFocus();
                }
            }
        });

        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked() == false) {
                    Toast.makeText(getApplicationContext(), "체크박스를 활성화해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pro_RegisterCodeActivity.this, Pro_MainActivity.class));
                finish();
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
                            String hashCode = messageData.child("uid").getValue().toString();
                            codeContent = hashCode.substring(0,6);

                            String input = code.getText().toString();

                            if (input.equals(codeContent)) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = user.getUid();
                                DatabaseReference myUserReference = ref.child("protector").child(uid);

                                Map<String, Object> userReg = new HashMap<>();
                                userReg.put("myUser", hashCode);
                                myUserReference.updateChildren(userReg);

                                DatabaseReference myProtectorReference = ref.child("user").child(hashCode);
                                Map<String, Object> proReg = new HashMap<>();
                                proReg.put("myProtector", uid);
                                myProtectorReference.updateChildren(proReg);

                                Toast.makeText(getApplicationContext(), "사용자 등록이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Pro_RegisterCodeActivity.this, Pro_MainActivity.class));
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