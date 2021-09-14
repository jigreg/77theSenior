package com.example.logintext.protector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.example.logintext.common.LoginActivity;
import com.example.logintext.common.LoginMaintainService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Pro_MainActivity extends AppCompatActivity {

    private Button gps, userReg, logout,calendar, setting;
    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private String uid, myUser, format_time, mywalk, mytrain;
    private SimpleDateFormat format;
    private Calendar time;
    private TextView walkstep, brain_train;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_main);

        logout = (Button) findViewById(R.id.logout);
        gps = (Button) findViewById(R.id.locationTrace);
        userReg = (Button) findViewById(R.id.userRegister);
        calendar = (Button) findViewById(R.id.myUserCalendar);
        setting = (Button) findViewById(R.id.setting);

        walkstep = (TextView) findViewById(R.id.walk_step);
        brain_train = (TextView) findViewById(R.id.brain_train);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
        mReference = mDatabase.getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        format = new SimpleDateFormat("yyyybMbd");
        time = Calendar.getInstance();

        format_time = format.format(time.getTime());

        mReference.child("protector").child(uid).child("myUser").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                myUser = task.getResult().getValue().toString();
                if (!myUser.equals("none")) {
                    mReference.child("user").child(myUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mywalk = (String.valueOf(snapshot.child("walk").child("date").child(format_time).child("walk").getValue()));
                            if (mywalk.equals("null")) {
                                walkstep.setText("오늘은 걷지 않았어요");
                            } else {
                                walkstep.setText(mywalk + "걸음");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    walkstep.setText("사용자와 연동해주세요.");
                }
            }
        });

        mReference.child("protector").child(uid).child("myUser").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                myUser = task.getResult().getValue().toString();
                if (!myUser.equals("none")) {
                    mReference.child("user").child(myUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mytrain = dataSnapshot.child("today_training").getValue().toString();
                            if (mytrain.equals("0")) {
                                brain_train.setText("오늘도 훈련해봐요");
                            } else {
                                brain_train.setText(mytrain + " 점");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    brain_train.setText("사용자와 연동해주세요.");
                }
            }
        });

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
                if (!myUser.equals("none")) {
                    startActivity(new Intent(Pro_MainActivity.this, Pro_LocationActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "사용자가 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        calendar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!myUser.equals("none")){
                    startActivity(new Intent(Pro_MainActivity.this, Pro_CalendarActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"사용자가 등록되지 않았습니다.",Toast.LENGTH_SHORT).show();

                }

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

        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //intent함수를 통해 register액티비티 함수를 호출한다.
                startActivity(new Intent(Pro_MainActivity.this, Pro_SettingActivity.class));
                finish();
            }
        });
    }
}
