package com.example.logintext.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logintext.R;

import java.util.ArrayList;

public class User_SpaceActivity extends AppCompatActivity {

    Button cbtn1, cbtn2, cbtn3, cbtn4;
    ImageView block;
    TextView matter;

    int[] pname = {R.drawable.space1, R.drawable.space2, R.drawable.space3};
    int[] mname = {R.string.one,R.string.two,R.string.three};
    String[][] cname = {{"5","6","7","8"},{"10","11","12","13"},{"10","12","13","15"}};
    String[] collect = {"4","3","3"};

    int puzzle;
    ArrayList<Integer> bpuzzle = new ArrayList<Integer>();

    boolean exist;
    String select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_space);

        cbtn1 = (Button)findViewById(R.id.cbtn1);
        cbtn2 = (Button)findViewById(R.id.cbtn2);
        cbtn3 = (Button)findViewById(R.id.cbtn3);
        cbtn4 = (Button)findViewById(R.id.cbtn4);

        block = (ImageView)findViewById(R.id.block);
        matter = (TextView)findViewById(R.id.matter);

        bpuzzle.add(-1);
        callmatter();

        cbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = "1";
                if (select == collect[puzzle]){
                    Toast.makeText(getApplicationContext(),"정답입니다",Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getApplicationContext(),"오답입니다",Toast.LENGTH_SHORT).show();
                callmatter();

            }
        });

        cbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = "2";
                if (select == collect[puzzle]){
                    Toast.makeText(getApplicationContext(),"정답입니다",Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getApplicationContext(),"오답입니다",Toast.LENGTH_SHORT).show();
                callmatter();
            }
        });

        cbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = "3";
                if (select == collect[puzzle]){
                    Toast.makeText(getApplicationContext(),"정답입니다",Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getApplicationContext(),"오답입니다",Toast.LENGTH_SHORT).show();
                callmatter();
            }
        });

        cbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = "4";
                if (select == collect[puzzle]){
                    Toast.makeText(getApplicationContext(),"정답입니다",Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getApplicationContext(),"오답입니다",Toast.LENGTH_SHORT).show();
                callmatter();
            }
        });


    }

    void callmatter(){
        puzzle = (int)(Math.random()*3);
        exist = false;
        for (int i = 0; i < bpuzzle.size();i++){
            if (puzzle == bpuzzle.get(i)) {
                exist = true;
            }
        }

        if(!exist) {
            block.setImageResource(pname[puzzle]);
            matter.setText(mname[puzzle]);
            cbtn1.setText(cname[puzzle][0]);
            cbtn2.setText(cname[puzzle][1]);
            cbtn3.setText(cname[puzzle][2]);
            cbtn4.setText(cname[puzzle][3]);

            bpuzzle.add(puzzle);
        }
        else{
            callmatter();
        }

        if(bpuzzle.size() == pname.length){
            bpuzzle.clear();
        }


    }
}