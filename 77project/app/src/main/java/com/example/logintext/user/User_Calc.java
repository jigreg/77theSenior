package com.example.logintext.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.logintext.R;

public class User_Calc extends AppCompatActivity {
    TextView num1,num2,sign;
    EditText input;
    String signs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_calc);

        num1 = (TextView) findViewById(R.id.number1);
        num2 = (TextView) findViewById(R.id.number2);
        sign = (TextView)findViewById(R.id.sign);
        input = (EditText) findViewById(R.id.input);


        int n1 = (int)(Math.random()*10)+1;
        int n2 = (int)(Math.random()*10)+1;
        int s = (int)(Math.random()*4);

        if (s ==0) signs = "+";
        else if(s ==1 && n1>n2) signs = "-";
        else if(s ==2 && n2<10) signs = "X";
        else if(s==3) signs = "%";



        num1.setText(Integer.toString(n1));
        num2.setText(Integer.toString(n2));
        sign.setText(signs);

    }
}