package com.example.logintext.common;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.example.logintext.user.User_CatFindActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindIdPwActivity extends AppCompatActivity {

    private ImageButton back;
    private EditText id_name, id_phonenum, id_birth, pw_name, pw_phonenum, pw_id, pw_birth;
    private RadioButton id_pro, id_user, pw_pro, pw_user;
    private RadioGroup id_userpro, pw_userpro;
    private Button id_check, pw_check;
    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference, nReference;
    private FirebaseAuth mAuth;
    private String uid, myUser, emailcheck, userpro;
    private List<AllData> DatainfoList = new ArrayList<>();
    private List<AllData1> DatainfoList1 = new ArrayList<>();
    private int user_check = 0;

    private class AllData {
        String name, phonenum, email, birth;

        AllData(String name, String birth, String phonenum, String email) {
            this.name = name;
            this.phonenum = phonenum;
            this.email = email;
            this.birth = birth;

        }

        public String getName() {
            return name;
        }

        public String getPhonenum() {
            return phonenum;
        }

        public String getEmail() {
            return email;
        }

        public String getBirth() {
            return birth;
        }
    }

    private class AllData1 {
        String name, phonenum, email, birth;

        AllData1(String name, String birth, String phonenum, String email) {
            this.name = name;
            this.phonenum = phonenum;
            this.email = email;
            this.birth = birth;

        }

        public String getName() {
            return name;
        }

        public String getPhonenum() {
            return phonenum;
        }

        public String getEmail() {
            return email;
        }

        public String getBirth() {
            return birth;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_pw);


        id_birth = (EditText) findViewById(R.id.id_birth);
        id_name = (EditText) findViewById(R.id.id_name);
        id_phonenum = (EditText) findViewById(R.id.id_phonenum);

        id_check = (Button) findViewById(R.id.id_check);
        id_pro = (RadioButton) findViewById(R.id.id_pro);
        id_user = (RadioButton) findViewById(R.id.id_user);
        id_userpro = (RadioGroup) findViewById(R.id.userpro);

        pw_birth = (EditText) findViewById(R.id.pw_birth);
        pw_name = (EditText) findViewById(R.id.pw_name);
        pw_phonenum = (EditText) findViewById(R.id.pw_phonenum);
        pw_id = (EditText) findViewById(R.id.pw_id);
        pw_check = (Button) findViewById(R.id.pw_check);
        pw_pro = (RadioButton) findViewById(R.id.pw_pro);
        pw_user = (RadioButton) findViewById(R.id.pw_user);
        pw_userpro = (RadioGroup) findViewById(R.id.pw_userpro);

        back = (ImageButton) findViewById(R.id.back);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
        mReference = mDatabase.getReference("Users").child("user");
        nReference = mDatabase.getReference("Users").child("protector");
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth.setLanguageCode("ko");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindIdPwActivity.this, LoginActivity.class));
                finish();
            }
        });

        id_userpro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.id_pro) {
                    userpro = "p";
                    user_check++;
                } else {
                    userpro = "u";
                    user_check++;
                }
            }
        });
        pw_userpro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pw_pro) {
                    userpro = "p";
                    user_check++;
                } else {
                    userpro = "u";
                    user_check++;
                }
            }
        });

        // user 데이터리스트
        mReference.orderByChild("uid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatainfoList.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String birth = messageData.child("birth").getValue().toString();
                    String name = messageData.child("name").getValue().toString();
                    String phonenum = messageData.child("phonenum").getValue().toString();
                    String email = messageData.child("email").getValue().toString();

                    DatainfoList.add(new AllData(name, birth, phonenum, email));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "onCancelled", Toast.LENGTH_SHORT);
            }
        });
        // protector 데이터리스트
        nReference.orderByChild("uid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatainfoList1.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String birth = messageData.child("birth").getValue().toString();
                    String name = messageData.child("name").getValue().toString();
                    String phonenum = messageData.child("phonenum").getValue().toString();
                    ;
                    String email = messageData.child("email").getValue().toString();

                    DatainfoList1.add(new AllData1(name, birth, phonenum, email));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "onCancelled", Toast.LENGTH_SHORT);
            }
        });

        //id check start
        id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_name.getText().toString().equals("") || id_name.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (id_birth.getText().toString().equals("") || id_birth.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (user_check == 0) {
                    Toast.makeText(getApplicationContext(), "버튼을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else if (id_phonenum.getText().toString().equals("") || id_phonenum.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "핸드폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (userpro.equals("u")) {
                    for (AllData D : DatainfoList) {
                        if (D.getName().equals(id_name.getText().toString()) && D.getBirth().equals(id_birth.getText().toString())
                                && D.getPhonenum().equals(id_phonenum.getText().toString())) {
                            emailcheck = D.getEmail();
                            showstartDialog();
                            break;
                        }
                    }
                } else if (userpro.equals("p")) {
                    for (AllData1 D : DatainfoList1) {
                        if (D.getName().equals(id_name.getText().toString()) && D.getBirth().equals(id_birth.getText().toString())
                                && D.getPhonenum().equals(id_phonenum.getText().toString())) {
                            emailcheck = D.getEmail();
                            showstartDialog();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "잘못된 정보를 입력하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        });//id check end

        //pw check start
        pw_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw_name.getText().toString().equals("") || pw_name.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (pw_id.getText().toString().equals("") || pw_id.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (pw_birth.getText().toString().equals("") || pw_birth.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (user_check == 0) {
                    Toast.makeText(getApplicationContext(), "버튼을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else if (pw_phonenum.getText().toString().equals("") || pw_phonenum.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "핸드폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (userpro.equals("u")) {
                    for (AllData D : DatainfoList) {
                        if (D.getName().equals(pw_name.getText().toString()) && D.getBirth().equals(pw_birth.getText().toString())
                                && D.getPhonenum().equals(pw_phonenum.getText().toString()) && D.getEmail().equals(pw_id.getText().toString())) {
                            mAuth.sendPasswordResetEmail(pw_id.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), pw_id + " 로 비밀번호 재설정 링크 전송", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            break;
                        }
                    }
                } else if(userpro.equals("p")){
                    for (AllData1 D : DatainfoList1) {
                        if (D.getName().equals(pw_name.getText().toString()) && D.getBirth().equals(pw_birth.getText().toString())
                                && D.getPhonenum().equals(pw_phonenum.getText().toString()) && D.getEmail().equals(pw_id.getText().toString())) {
                            mAuth.sendPasswordResetEmail(pw_id.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), pw_id.getText().toString() + " 로 비밀번호 재설정 링크 전송", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            break;
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "잘못된 정보를 입력하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });//pw check end

    }//oncreate end

    void showstartDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(FindIdPwActivity.this)
                .setTitle("ID 찾기")
                .setMessage("찾으시는 아이디는 " + emailcheck + " 입니다.")
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();

    }
}//public end
