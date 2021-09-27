package com.example.logintext.protector;

import static java.lang.String.format;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.example.logintext.user.User_MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Pro_CalendarActivity extends AppCompatActivity {

    private String name = null;
    private String uid = null;
    private String walkdata, myUser;
    private float dis;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private TextView myName, walkData, brainData, cal, distance, calorie;
    private ImageButton back;

    private CalendarView calendarView;
    private List<date2> dateList = new ArrayList<>();

    class date2 {
        String time;
        date2(String time) {
            this.time = time;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_calendar);

        myName = (TextView) findViewById(R.id.myName);
        cal = (TextView) findViewById(R.id.cal_Date);
        walkData = (TextView) findViewById(R.id.walkData);
        brainData = (TextView) findViewById(R.id.brainData);
        distance = (TextView) findViewById(R.id.distance);
        calorie = (TextView) findViewById(R.id.calorie);
        back = (ImageButton) findViewById(R.id.back);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //로그인 및 회원가입 엑티비티에서 이름을 받아옴
        mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
        mReference = mDatabase.getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();


        mReference.child("protector").child(uid).child("myUser").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                myUser = task.getResult().getValue().toString();

                if (!myUser.equals("none")) {
                    mReference.child("user").child(myUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            name = snapshot.child("name").getValue().toString();
                            myName.setText(name + "님의 캘린더");
                        }
                        @Override
                        public void onCancelled (@NonNull DatabaseError error){
                            Toast.makeText(getApplicationContext(), "onCancelled", Toast.LENGTH_SHORT);
                        }
                    });

                }
            }

        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                cal.setText(format("%d 년 %d 월 %d 일", year, month + 1, dayOfMonth));

                //저장된 걸음 수 가져오기
                String selectyear = Integer.toString(year);
                String selectmonth = Integer.toString(month + 1);
                String selectday = Integer.toString(dayOfMonth);
                String date = selectyear + selectmonth + selectday;

                mReference.child("user").child(myUser).child("walk").child("date").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        dateList.clear();
                        for (DataSnapshot messageData : snapshot.getChildren()) {
                            String time = messageData.child("time").getValue().toString();
                            dateList.add(new date2(time));
                        }

                        for (int i = 0; i < dateList.size(); i++) {
                            if (dateList.get(i).time.equals(date)) {
                                walkdata = snapshot.child(date).child("walk").getValue().toString();
                                walkData.setText(walkdata + " 걸음");

                                dis = Float.parseFloat(walkdata);
                                distance.setText(String.format("%.2f Km ", (dis * 0.0007f)));
                                calorie.setText(String.format("%.2f cal", (dis * 0.0374f)));
                                progressBar.setProgress(Integer.parseInt(walkdata));
                                break;
                            } else {
                                dis = 0;
                                distance.setText(String.format("%.2f Km ", (dis * 0.0007f)));
                                calorie.setText(String.format("%.2f cal", (dis * 0.0374f)));
                                progressBar.setProgress(0);
                                walkData.setText("걷지 않았어요");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "onCancelled", Toast.LENGTH_SHORT);
                    }
                });
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Pro_CalendarActivity.this, Pro_MainActivity.class));
                finish();
            }
        });
    }
}