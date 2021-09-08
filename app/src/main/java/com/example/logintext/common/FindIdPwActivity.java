package com.example.logintext.common;


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

import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
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
    private EditText id_name, id_phonenum, id_birth;
    private RadioButton id_male, id_female;
    private Button id_check;
    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference, nReference;
    private FirebaseAuth mAuth;
    private String uid, myUser, id_gender,id_email;
    private List<AllData> DatainfoList = new ArrayList<>();

    private class AllData {
        String name, phonenum, email, birth, gender;
        AllData(String name, String birth,String phonenum,String gender,String email) {
            this.name = name;
            this.gender = gender;
            this.phonenum = phonenum;
            this.email = email;
            this.birth = birth;

        }
        public String getName() { return name; }
        public String getPhonenum() { return phonenum; }
        public String getEmail() { return email; }
        public String getBirth() { return birth; }
        public String getGender() { return gender; }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_pw);

        id_birth = (EditText) findViewById(R.id.id_birth);
        id_name = (EditText) findViewById(R.id.id_name);
        id_phonenum = (EditText) findViewById(R.id.id_phonenum);

        id_check = (Button)findViewById(R.id.id_check);
        id_male = (RadioButton) findViewById(R.id.id_male);
        id_female = (RadioButton) findViewById(R.id.id_female);

        back = (ImageButton) findViewById(R.id.back);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
        mReference = mDatabase.getReference("Users").child("user");
        user = FirebaseAuth.getInstance().getCurrentUser();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindIdPwActivity.this, LoginActivity.class));
                finish();
            }
        });

        if ( id_male.isChecked() ) {
            id_gender = "m";
        } else if( id_female.isChecked() ) {
            id_gender = "f";
        }

        mReference.orderByChild("uid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatainfoList.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String birth = messageData.child("birth").getValue().toString();
                    String name = messageData.child("name").getValue().toString();
                    String phonenum = messageData.child("phonenum").getValue().toString();
                    String gender = messageData.child("gender").getValue().toString();
                    String email = messageData.child("email").getValue().toString();

                    DatainfoList.add(new AllData(name, birth, phonenum, gender, email));
                }
            }
                @Override
                public void onCancelled (DatabaseError databaseError){
                    Toast.makeText(getApplicationContext(), "onCancelled", Toast.LENGTH_SHORT);
                }
        });

        id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_name.getText().toString().equals("") || id_name.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (id_birth.getText().toString().equals("") || id_birth.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (id_phonenum.getText().toString().equals("") || id_phonenum.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "핸드폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!id_male.isChecked() && id_female.isChecked()) {
                    Toast.makeText(getApplicationContext(), "버튼을 체크해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    for (AllData D : DatainfoList) {
                        if (D.getName().equals(id_name.getText().toString()) && D.getBirth().equals(id_birth.getText().toString())
                                && D.getPhonenum().equals(id_phonenum.getText().toString())) { // && D.getGender().equals(id_gender)
                            id_email = D.getEmail();
                            Toast.makeText(getApplicationContext(), id_email, Toast.LENGTH_SHORT).show();
                            break;
                        } else {
//                            Toast.makeText(getApplicationContext(), DatainfoList.get(3).toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });//id check end

    }//oncreate end
}//public end
