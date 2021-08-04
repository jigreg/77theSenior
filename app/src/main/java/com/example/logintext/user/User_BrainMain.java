package com.example.logintext.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.R;

public class User_BrainMain extends AppCompatActivity {

    private ImageButton back;
    private Button cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_brain_main);

        back = (ImageButton) findViewById(R.id.back);
        cat = (Button)findViewById(R.id.FindCat);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_BrainMain.this, User_MainActivity.class));
                finish();
            }
        });

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_BrainMain.this, User_CatFindActivity.class));
                finish();
            }
        });

    }
}