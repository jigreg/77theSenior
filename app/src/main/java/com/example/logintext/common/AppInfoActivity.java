package com.example.logintext.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;
import com.example.logintext.protector.Pro_MainActivity;
import com.example.logintext.protector.Pro_SettingActivity;
import com.example.logintext.user.User_MainActivity;
import com.example.logintext.user.User_SettingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppInfoActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser user;

    private String uid;

    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.associate);

        mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
        mReference = mDatabase.getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        try {
                            String user_type = task.getResult().child("user").child(uid).child("type").getValue().toString();
                            if (user_type.equals("user")) {
                                startActivity(new Intent(AppInfoActivity.this, User_SettingActivity.class));
                                finish();
                            }
                        } catch (NullPointerException e) {
                            String pro_type = task.getResult().child("protector").child(uid).child("type").getValue().toString();
                            if (pro_type.equals("protector")) {
                                startActivity(new Intent(AppInfoActivity.this, Pro_SettingActivity.class));
                                finish();
                            }
                        }
                    }
                });
            }
        });
    }
}
