package com.example.logintext.user;

import android.Manifest;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.logintext.R;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation")
public class User_DiagnosisActivity extends TabActivity implements SensorEventListener {

    private ImageButton back;
    private LinearLayout timeCountSettingLV, timeCountLV;
    private EditText hourET, minuteET, secondET;
    private TextView hourTV, minuteTV, secondTV, finishTV;
    private Button startBtn;
    private int hour, minute, second;
    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    private TextView stepCountView;
    private Button resetButton;

    private TabHost tabHost;

    private int currentSteps = 0;

    @RequiresApi(api = Build.VERSION_CODES.Q)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_diagnosis);

        tabHost = getTabHost();

        TabHost.TabSpec tabSpecWalk = tabHost.newTabSpec("WALKING").setIndicator("걸음별");
        tabSpecWalk.setContent(R.id.walking);
        tabHost.addTab(tabSpecWalk);

        TabHost.TabSpec tabSpecTrain = tabHost.newTabSpec("TRAINING").setIndicator("점수별");
        tabSpecTrain.setContent(R.id.training);
        tabHost.addTab(tabSpecTrain);

        tabHost.setCurrentTab(0);

        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_DiagnosisActivity.this, User_MainActivity.class));
                finish();
            }
        });

        timeCountSettingLV = (LinearLayout)findViewById(R.id.timeCountSettingLV);
        timeCountLV = (LinearLayout)findViewById(R.id.timeCountLV);

        hourET = (EditText)findViewById(R.id.hourET);
        minuteET = (EditText)findViewById(R.id.minuteET);
        secondET = (EditText)findViewById(R.id.secondET);

        hourTV = (TextView)findViewById(R.id.hourTV);
        minuteTV = (TextView)findViewById(R.id.minuteTV);
        secondTV = (TextView)findViewById(R.id.secondTV);
        finishTV = (TextView)findViewById(R.id.finishTV);
        stepCountView = (TextView)findViewById(R.id.diagnosiswalk);

        startBtn = (Button)findViewById(R.id.startBtn);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);


        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        // 시작버튼 이벤트 1처리
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeCountSettingLV.setVisibility(View.GONE);
                timeCountLV.setVisibility(View.VISIBLE);

                hourTV.setText(hourET.getText().toString());
                minuteTV.setText(minuteET.getText().toString());
                secondTV.setText(secondET.getText().toString());

                hour = Integer.parseInt(hourET.getText().toString());
                minute = Integer.parseInt(minuteET.getText().toString());
                second = Integer.parseInt(secondET.getText().toString());

                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        // 반복실행할 구문

                        // 0초 이상이면
                        if(second != 0) {
                            //1초씩 감소
                            second--;

                            // 0분 이상이면
                        } else if(minute != 0) {
                            // 1분 = 60초
                            second = 60;
                            second--;
                            minute--;

                            // 0시간 이상이면
                        } else if(hour != 0) {
                            // 1시간 = 60분
                            second = 60;
                            minute = 60;
                            second--;
                            minute--;
                            hour--;
                        }

                        //시, 분, 초가 10이하(한자리수) 라면
                        // 숫자 앞에 0을 붙인다 ( 8 -> 08 )
                        if(second <= 9){
                            secondTV.setText("0" + second);
                        } else {
                            secondTV.setText(Integer.toString(second));
                        }

                        if(minute <= 9){
                            minuteTV.setText("0" + minute);
                        } else {
                            minuteTV.setText(Integer.toString(minute));
                        }

                        if(hour <= 9){
                            hourTV.setText("0" + hour);
                        } else {
                            hourTV.setText(Integer.toString(hour));
                        }

                        // 시분초가 다 0이라면 toast를 띄우고 타이머를 종료한다..
                        if(hour == 0 && minute == 0 && second == 0) {
                            timer.cancel();//타이머 종료
                            //walkDialog();
                            currentSteps = 0;
                           // finishTV.setText("타이머가 종료되었습니다.");
                        }
                    }
                };

                //타이머를 실행
                timer.schedule(timerTask, 0, 1000); //Timer 실행
                onStart();
            }
        });



    }//oncreate end

    public void onStart() {
        super.onStart();
        if(stepCountSensor !=null) {
            // 센서 속도 설정
            // * 옵션
            // - SENSOR_DELAY_NORMAL: 20,000 초 딜레이
            // - SENSOR_DELAY_UI: 6,000 초 딜레이
            // - SENSOR_DELAY_GAME: 20,000 초 딜레이
            // - SENSOR_DELAY_FASTEST: 딜레이 없음
            //
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        // 걸음 센서 이벤트 발생시
        if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){

            if(event.values[0]==1.0f){
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                currentSteps++;
                stepCountView.setText(String.valueOf(currentSteps));
            }

        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void walkDialog() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(User_DiagnosisActivity.this)
                .setTitle("걷기 정보")
                .setMessage("걸음 수: "+ stepCountView + "시간 : ")
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }


}//final end
