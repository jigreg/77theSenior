package com.example.logintext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class BrainMain extends AppCompatActivity {

    ImageButton before;
    Button cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brain_main);

        before = (ImageButton) findViewById(R.id.bbefore);
        cat = (Button)findViewById(R.id.FindCat);

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BrainMain.this, UserMainActivity.class));
                finish();
            }
        });

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BrainMain.this, CatFindActivity.class));
                finish();
            }
        });



    }
}