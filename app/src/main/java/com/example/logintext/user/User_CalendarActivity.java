package com.example.logintext.user;

import static java.lang.String.format;

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
import com.example.logintext.protector.Pro_GPSHistoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class User_CalendarActivity extends AppCompatActivity {

    private String name = null;
    private String uid = null;
    private String date1, walkdata;

    private TextView myName, walkData, brainData, cal;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
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
                cal.setText(format("%d 년 %d 월 %d 일",year,month+1,dayOfMonth));
                //저장된 걸음 수 가져오기
                date1 = format("%d년 %d월 %d일",year,month+1,dayOfMonth);

                ref.child(uid).child("walk").child("date").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        dateList.clear();
                        for (DataSnapshot messageData : snapshot.getChildren()) {
                            String time = messageData.child("time").getValue().toString();
                            dateList.add(new date2(time));
                        }
                        //  date2.add(snapshot.child("walk").child("date").getValue().toString());
                        Toast.makeText(getApplicationContext(), date1.equals("2021년 08월 10일")+"", Toast.LENGTH_SHORT).show();
                        //dateList에 날짜만 넣는 방법이 없을까? ex) [2021년 08월 09일, 2021년 08월 10일]

                        if (dateList.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "데이터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        } else if (dateList.get(1).time.equals(date1)) {
                            Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                            walkdata = snapshot.child("walk").child("date").child(date1).child("walk").getValue().toString();
                        } else {
                            Toast.makeText(getApplicationContext(), "해당 날짜에 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                            walkdata = "0";
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),"onCancelled", Toast.LENGTH_SHORT);
                    }
                });
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
