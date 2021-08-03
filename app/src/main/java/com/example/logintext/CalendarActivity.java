    package com.example.logintext;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.annotation.SuppressLint;
    import android.content.ContentValues;
    import android.content.Intent;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CalendarView;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.io.FileInputStream;
    import java.io.FileOutputStream;

    public class CalendarActivity extends AppCompatActivity {

        public String name=null;
        public String uid=null;
        public CalendarView calendarView;
        public Button cha_Btn,del_Btn,save_Btn;
        public TextView diaryTextView,textView2,textView3, wd, bd;
        public EditText contextEditText;
        ImageButton before;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.calendar);
            calendarView=findViewById(R.id.calendarView);
            textView3=findViewById(R.id.textView3);
            wd = findViewById(R.id.walkdata);
            bd = findViewById(R.id.braindata);
            before = (ImageButton) findViewById(R.id.before4);

            //로그인 및 회원가입 엑티비티에서 이름을 받아옴
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            uid = user.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Users").child("user");
            ref.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    name = snapshot.child("name").getValue().toString();
                    textView3.setText(name+"님의 캘린더");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"onCancelled", Toast.LENGTH_SHORT);
                }
            });


            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    diaryTextView.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    diaryTextView.setText(String.format("%d / %d / %d",year,month+1,dayOfMonth));
                    contextEditText.setText("");
                }
            });
            before.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CalendarActivity.this, UserMainActivity.class));
                }
            });
            //날짜 클릭했을 때 그 날짜 인식하기 --> 날짜 클릭시 그 날짜를 파베 데이터에 있는 날짜와 비교? 후에 없으면 0걸음 표시? 파베에 날짜별로 저장을 안함 어카지? 시발~
            //로그인 된 회원의 데이터 불러오기 -- > 위랑 똑같이 하면 되는데 날짜별로 걷기데이터랑 두뇌훈련 점수 저장되어야함 어 시발 좆같네
            //인식된 날짜의 걷기 데이터와 두뇌훈련 데이터 불러오기 --> 데이터 없으면 0걸음 0점수 표시 근데 그 밑에 차트랑 그거 어케함 시발^^

        }



   }