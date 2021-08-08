package com.example.logintext.common;

import com.example.logintext.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.protector.Pro_MainActivity;
import com.example.logintext.user.User_MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser user;

    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    // 이메일과 비밀번호
    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email = "";
    private String password = "";
    private String uid;

    private Button regst, idpw;
    private CheckBox autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        regst = (Button) findViewById(R.id.regst);
        idpw = (Button) findViewById(R.id.idPw);

        editTextEmail = (EditText) findViewById(R.id.et_email);
        editTextPassword = (EditText) findViewById(R.id.et_password);

        autoLogin = (CheckBox) findViewById(R.id.autoLogin);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        regst.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                startActivity(new Intent(LoginActivity.this, ChooseActivity.class));
                finish();
            }
        });

        idpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FindIdPwActivity.class));
                finish();
            }
        });
    }

    public void signIn(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            loginUser(email, password);
        }
    }

    // 이메일 유효성 검사
    private boolean isValidEmail() {
        if (email.isEmpty()) {
            // 이메일 공백
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(LoginActivity.this, "이메일 형식과 맞지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 로그인
    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
                            mReference = mDatabase.getReference("Users");
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            uid = user.getUid();

                            mReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (autoLogin.isChecked()) {
                                        if (LoginMaintainService.getUserName(LoginActivity.this).length() == 0) {
                                            // 저장 정보가 없을 경우
                                            LoginMaintainService.setUserName(LoginActivity.this, editTextEmail.getText().toString());
                                        } else {
                                            // 저장 정보가 있을 경우 -> 다음 화면 호출
                                            try {
                                                String user_type = task.getResult().child("user").child(uid).child("type").getValue().toString();
                                                if (user_type.equals("user")) {

                                                    Intent intent = new Intent(LoginActivity.this, User_MainActivity.class);
                                                    intent.putExtra("STD_NUM", LoginMaintainService.getUserName(LoginActivity.this).toString());
                                                    startActivity(intent);
                                                    finish();

                                                    Toast.makeText(LoginActivity.this, "유저 자동 로그인 ON", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (NullPointerException e) {
                                                String pro_type = task.getResult().child("protector").child(uid).child("type").getValue().toString();
                                                if (pro_type.equals("protector")) {

                                                    Intent intent = new Intent(LoginActivity.this, Pro_MainActivity.class);
                                                    intent.putExtra("STD_NUM", LoginMaintainService.getUserName(LoginActivity.this).toString());
                                                    startActivity(intent);
                                                    finish();

                                                    Toast.makeText(LoginActivity.this, "보호자 자동 로그인 ON", Toast.LENGTH_SHORT).show();
                                                }
                                            }
//                                            Toast.makeText(LoginActivity.this, R.string.success_login + "자동 로그인 ON", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        try {
                                            String user_type = task.getResult().child("user").child(uid).child("type").getValue().toString();
                                            if (user_type.equals("user")) {
                                                startActivity(new Intent(LoginActivity.this, User_MainActivity.class));
                                                finish();
                                            }
                                        } catch (NullPointerException e) {
                                            String pro_type = task.getResult().child("protector").child(uid).child("type").getValue().toString();
                                            if (pro_type.equals("protector")) {
                                                startActivity(new Intent(LoginActivity.this, Pro_MainActivity.class));
                                                finish();
                                            }
                                        }
                                        Toast.makeText(LoginActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // 로그인 실패
                            Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}