package com.example.logintext;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.stream.IntStream;

public class CatFindActivity extends AppCompatActivity {
    Button btnStart;
    TableLayout t1,t2;
    ImageView [] IV = new ImageView[20];
    ImageButton [] IB = new ImageButton[20];
    ImageView f1,f2,f3;
    TextView tv;

    Integer[] IVID = {R.id.a1,R.id.a2,R.id.a3,R.id.a4,R.id.a5,R.id.a6,R.id.a7,R.id.a8,R.id.a9,R.id.a10,R.id.a11,R.id.a12,R.id.a13,R.id.a14,R.id.a15,R.id.a16,R.id.a17,R.id.a18,R.id.a19,R.id.a20};
    Integer[] IVIB = {R.id.box1,R.id.box2,R.id.box3,R.id.box4,R.id.box5,R.id.box6,R.id.box7,R.id.box8,R.id.box9,R.id.box10,R.id.box11,R.id.box12,R.id.box13,R.id.box14,R.id.box15,R.id.box16,R.id.box17,R.id.box18,R.id.box19,R.id.box20};
    Integer[] D = {R.drawable.cat1,R.drawable.cat2,R.drawable.cat3,R.drawable.cat4,R.drawable.cat5,R.drawable.cat6};
    int i;
    static int[] ri= new int[6];

    static Random r = new Random();
    int cnt1,cnt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brain_cat);
        getSupportActionBar().setTitle("Cat in the box");

        btnStart = (Button) findViewById(R.id.btnStart);

        t1 = (TableLayout) findViewById(R.id.t1);
        t2 = (TableLayout) findViewById(R.id.t2);
        f1 = (ImageView) findViewById(R.id.f1);
        f2 = (ImageView) findViewById(R.id.f2);
        f3 = (ImageView) findViewById(R.id.f3);
        tv = (TextView) findViewById(R.id.tv);

        for (int i = 0; i < 20; i++) {
            IV[i] = (ImageView) findViewById(IVID[i]);
        }
        for (int i = 0; i < 20; i++) {
            IB[i] = (ImageButton) findViewById(IVIB[i]);
        }


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt1 = 0;
                cnt2 = 3;
                tv.setText("");
                f3.setVisibility(View.VISIBLE);
                f2.setVisibility(View.VISIBLE);
                f1.setVisibility(View.VISIBLE);
                t1.setVisibility(View.VISIBLE);
                t2.setVisibility(View.INVISIBLE);
                for (int i = 0; i < 20; i++) {
                    IB[i].setVisibility(View.VISIBLE);
                    IV[i].setImageResource(0);
                }

                for (int i = 0; i < 6; i++) {
                    ri[i] = r.nextInt(20);
                    for (int j = 0; j < i; j++) {
                        if (ri[i] == ri[j])
                            i--;
                    }
                }
                IV[ri[0]].setImageResource(D[0]);
                IV[ri[1]].setImageResource(D[1]);
                IV[ri[2]].setImageResource(D[2]);
                IV[ri[3]].setImageResource(D[3]);
                IV[ri[4]].setImageResource(D[4]);
                IV[ri[5]].setImageResource(D[5]);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 20; i++) {
                            IV[i].setImageResource(0);
                        }
                        for (int i = 0; i < 6; i++) {
                            ri[i] = r.nextInt(20);
                            for (int j = 0; j < i; j++) {
                                if (ri[i] == ri[j])
                                    i--;
                            }
                        }
                        IV[ri[0]].setImageResource(D[0]);
                        IV[ri[1]].setImageResource(D[1]);
                        IV[ri[2]].setImageResource(D[2]);
                        IV[ri[3]].setImageResource(D[3]);
                        IV[ri[4]].setImageResource(D[4]);
                        IV[ri[5]].setImageResource(D[5]);
                    }
                }, 1000);
                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 20; i++) {
                            IV[i].setImageResource(0);
                        }
                        for (int i = 0; i < 6; i++) {
                            ri[i] = r.nextInt(20);
                            for (int j = 0; j < i; j++) {
                                if (ri[i] == ri[j])
                                    i--;
                            }
                        }
                        IV[ri[0]].setImageResource(D[0]);
                        IV[ri[1]].setImageResource(D[1]);
                        IV[ri[2]].setImageResource(D[2]);
                        IV[ri[3]].setImageResource(D[3]);
                        IV[ri[4]].setImageResource(D[4]);
                        IV[ri[5]].setImageResource(D[5]);
                    }
                }, 2000);
                Handler handler3 = new Handler();
                handler3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        t2.setVisibility(View.VISIBLE);
                    }
                }, 3000);
            }
        });

        for (i = 0; i < IB.length; i++) {
            final int index;
            index = i;

            IB[index].setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    IB[index].setVisibility(View.INVISIBLE);
                    if (IntStream.of(ri).anyMatch(x -> x == index)) {
                        cnt1++;
                        if (cnt1 == 6) {
                            tv.setText("WIN");
                            t2.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                IB[index].setVisibility(View.VISIBLE);
                            }
                        }, 1000);
                        cnt2--;
                        if (cnt2 == 2) {
                            f3.setVisibility(View.INVISIBLE);
                        } else if (cnt2 == 1) {
                            f2.setVisibility(View.INVISIBLE);
                        } else if (cnt2 == 0) {
                            f1.setVisibility(View.INVISIBLE);
                            t2.setVisibility(View.INVISIBLE);
                            tv.setText("DEFEAT");
                        }
                    }
                }
            });
        }
    }
}