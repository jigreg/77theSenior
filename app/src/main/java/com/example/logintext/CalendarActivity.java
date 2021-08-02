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

        public String fname=null;
        public String str=null;
        public String name=null;
        public String uid=null;
        public CalendarView calendarView;
        public Button cha_Btn,del_Btn,save_Btn;
        public TextView diaryTextView,textView2,textView3;
        public EditText contextEditText;
        ImageButton before;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.calendar);
            calendarView=findViewById(R.id.calendarView);
            diaryTextView=findViewById(R.id.diaryTextView);
            save_Btn=findViewById(R.id.save_Btn);
            del_Btn=findViewById(R.id.del_Btn);
            cha_Btn=findViewById(R.id.cha_Btn);
            textView2=findViewById(R.id.textView2);
            textView3=findViewById(R.id.textView3);
            contextEditText=findViewById(R.id.contextEditText);
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
                    checkDay(year,month,dayOfMonth,name);
                }
            });
            before.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CalendarActivity.this, UserMainActivity.class));
                }
            });
            save_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveDiary(fname);
                    str=contextEditText.getText().toString();
                    textView2.setText(str);
                    save_Btn.setVisibility(View.INVISIBLE);
                    cha_Btn.setVisibility(View.VISIBLE);
                    del_Btn.setVisibility(View.VISIBLE);
                    contextEditText.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.VISIBLE);

                }
            });
        }

        public void  checkDay(int cYear,int cMonth,int cDay,String userID){
            fname=""+userID+cYear+"-"+(cMonth+1)+""+"-"+cDay+".txt";//저장할 파일 이름설정
            FileInputStream fis=null;//FileStream fis 변수

            try{
                fis=openFileInput(fname);

                byte[] fileData=new byte[fis.available()];
                fis.read(fileData);
                fis.close();

                str=new String(fileData);

                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView2.setText(str);

                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);

                cha_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        contextEditText.setVisibility(View.VISIBLE);
                        textView2.setVisibility(View.INVISIBLE);
                        contextEditText.setText(str);

                        save_Btn.setVisibility(View.VISIBLE);
                        cha_Btn.setVisibility(View.INVISIBLE);
                        del_Btn.setVisibility(View.INVISIBLE);
                        textView2.setText(contextEditText.getText());
                    }

                });
                del_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textView2.setVisibility(View.INVISIBLE);
                        contextEditText.setText("");
                        contextEditText.setVisibility(View.VISIBLE);
                        save_Btn.setVisibility(View.VISIBLE);
                        cha_Btn.setVisibility(View.INVISIBLE);
                        del_Btn.setVisibility(View.INVISIBLE);
                        removeDiary(fname);
                    }
                });
                if(textView2.getText()==null){
                    textView2.setVisibility(View.INVISIBLE);
                    diaryTextView.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    contextEditText.setVisibility(View.VISIBLE);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @SuppressLint("WrongConstant")
        public void removeDiary(String readDay){
            FileOutputStream fos=null;

            try{
                fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
                String content="";
                fos.write((content).getBytes());
                fos.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @SuppressLint("WrongConstant")
        public void saveDiary(String readDay){
            FileOutputStream fos=null;

            try{
                fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
                String content=contextEditText.getText().toString();
                fos.write((content).getBytes());
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }