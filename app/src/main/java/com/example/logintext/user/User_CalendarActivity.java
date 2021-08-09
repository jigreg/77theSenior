package com.example.logintext.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_CalendarActivity extends AppCompatActivity {

    private String name = null;
    private String uid = null;

    private TextView myName, walkData, brainData, cal;
    private ImageButton back;

    private CalendarView calendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_calendar);

        myName = (TextView) findViewById(R.id.myName);
        cal = (TextView) findViewById(R.id.cal_Date);
        walkData = (TextView) findViewById(R.id.walkData);
        brainData = (TextView) findViewById(R.id.brainData);
        back = (ImageButton) findViewById(R.id.back);
        calendarView = (CalendarView) findViewById(R.id.calendarView);

        //로그인 및 회원가입 엑티비티에서 이름을 받아옴
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users").child("user");
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue().toString();
                myName.setText(name+"님의 캘린더");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"onCancelled", Toast.LENGTH_SHORT);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                cal.setText(String.format("%d 년 %d 월 %d 일",year,month+1,dayOfMonth));
                }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_CalendarActivity.this, User_MainActivity.class));
                finish();
            }
        });

        //날짜 클릭했을 때 그 날짜 인식하기 --> 날짜 클릭시 그 날짜를 파베 데이터에 있는 날짜와 비교? 후에 없으면 0걸음 표시? 파베에 날짜별로 저장을 안함 어카지? 야호~
        //로그인 된 회원의 데이터 불러오기 -- > 위랑 똑같이 하면 되는데 날짜별로 걷기데이터랑 두뇌훈련 점수 저장되어야함 어 야호 신나네
        //인식된 날짜의 걷기 데이터와 두뇌훈련 데이터 불러오기 --> 데이터 없으면 0걸음 0점수 표시 근데 그 밑에 차트랑 그거 어케함 야호^^

    }



}