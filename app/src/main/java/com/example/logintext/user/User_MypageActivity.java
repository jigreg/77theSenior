package com.example.logintext.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class User_MypageActivity extends AppCompatActivity {

   private ImageButton back;
   private Button proinfo;
   private TextView allrank,allpercent,walkrank,walkpercent,brainrank,brainpercent,grade;
   private String uid, proname, prophonenum, prouid, mywalkrank,mytrainrank,mywalkpercent,mytrainpercent,allmyrank,allmypercent;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.user_mypage);


       back = (ImageButton)findViewById(R.id.back);
       proinfo = (Button)findViewById(R.id.proinfo);
       allrank = (TextView)findViewById(R.id.allrank);
       allpercent = (TextView)findViewById(R.id.allpercent);
       walkrank = (TextView)findViewById(R.id.walkrank);
       walkpercent = (TextView)findViewById(R.id.walkpercent);
       brainrank = (TextView)findViewById(R.id.brainrank);
       brainpercent = (TextView)findViewById(R.id.brainpercent);
       grade = (TextView)findViewById(R.id.grade);


       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       uid = user.getUid();
       FirebaseDatabase database = FirebaseDatabase.getInstance();
       DatabaseReference nreference = database.getReference("Users").child("user");
       DatabaseReference mreference = database.getReference("Users").child("protector");

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(User_MypageActivity.this, User_MainActivity.class));
               finish();
           }
       });

       proinfo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               nreference.child(uid).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       prouid = snapshot.child("myProtector").getValue().toString();
                       if (prouid.equals("none")) {
                           prosetDialog();
                       } else {
                           mreference.child(prouid).addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   proname = snapshot.child("name").getValue().toString();
                                   prophonenum = snapshot.child("phonenum").getValue().toString();
                                   proinfoDialog();
                               }
                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {
                               }
                           });
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {
                   }
               });
           }
       });

       nreference.child(uid).child("today").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.getValue().equals("none") && snapshot.getValue()== null){
                    RankDialog();
               }else{
                   mywalkrank = snapshot.child("today_walkrank").getValue().toString();
                   mytrainrank = snapshot.child("today_trainrank").getValue().toString();
                   allmyrank = String.valueOf((Integer.parseInt(mywalkrank) + Integer.parseInt(mytrainrank))/2);
                   mywalkpercent = snapshot.child("today_walkpercent").getValue().toString();
                   mytrainpercent = snapshot.child("today_trainpercent").getValue().toString();
                   allmypercent = String.valueOf((Integer.parseInt(mywalkpercent) + Integer.parseInt(mytrainpercent))/2);
                   walkrank.setText(mywalkrank + "위");
                   brainrank.setText(mytrainrank+ "위");
                   walkpercent.setText("상위 " +mywalkpercent + "%");
                   brainpercent.setText("상위 " +mytrainpercent + "%");
                   allrank.setText(allmyrank + "위");
                   allpercent.setText("상위 "+allmypercent +"%");
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });



   }//oncreate end

   //알림창
   void proinfoDialog() {
       AlertDialog.Builder msgBuilder = new AlertDialog.Builder(User_MypageActivity.this)
               .setTitle("보호자 정보")
               .setMessage("보호자 성함은 " + proname + " 입니다." +"\n"+ "보호자 전화번호는 " + prophonenum + " 입니다.")
               .setPositiveButton("확인",
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {

                           }
                       });
       AlertDialog msgDlg = msgBuilder.create();
       msgDlg.show();

   }
   void prosetDialog() {
       AlertDialog.Builder msgBuilder = new AlertDialog.Builder(User_MypageActivity.this)
               .setTitle("보호자 등록")
               .setMessage("보호자가 등록되지 않았습니다."+"\n"+"확인을 누르면 보호자 등록화면으로 이동됩니다.")
               .setPositiveButton("확인",
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               startActivity(new Intent(User_MypageActivity.this, User_RegisterCodeActivity.class));
                               finish();
                           }
                       });
       AlertDialog msgDlg = msgBuilder.create();
       msgDlg.show();

   }
    void RankDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(User_MypageActivity.this)
                .setTitle("순위갱신")
                .setMessage("순위가 갱신되지 않았습니다."+"\n"+"버튼을 누르면 순위화면으로 이동되어 갱신됩니다.")
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(User_MypageActivity.this, User_RankActivity.class));
                                finish();
                            }
                        });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();

    }
}//main end