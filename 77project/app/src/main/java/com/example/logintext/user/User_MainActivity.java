package com.example.logintext.user;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.logintext.PushAlarmReceiver;
import com.example.logintext.R;
import com.example.logintext.RankRegisterReceiver;
import com.example.logintext.UndeadService;
import com.example.logintext.common.LoginActivity;
import com.example.logintext.common.LoginMaintainService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class User_MainActivity extends AppCompatActivity {

    private Button logout, walk, training, locate, setting, ranking, calendar,mypage;
    private TextView walkstep, brain_train;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference ref, mReference, nReference;

    private String uid, format_time, mywalk, mytrain;

    private AlarmManager alarmManager, rankManager;
    private PendingIntent pendingPush, pendingRanking;

    private SimpleDateFormat format;
    private Calendar time;
    private ArrayList<NewsItem> arrayList;
    private NewsAdapter adapter;
    private ListView listView;

    private StringBuilder news_title;
    private StringBuilder news_link;

    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        logout = (Button) findViewById(R.id.logout);
        walk = (Button) findViewById(R.id.walking);
        training = (Button) findViewById(R.id.brainTraining);
        locate = (Button) findViewById(R.id.locate);
        ranking = (Button) findViewById(R.id.ranking);
        setting = (Button) findViewById(R.id.setting);
        calendar = (Button) findViewById(R.id.calendar);
        mypage = (Button) findViewById(R.id.myPage);

        walkstep = (TextView) findViewById(R.id.walk_step);
        brain_train = (TextView) findViewById(R.id.brain_train);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        rankManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        setPushAlarm();
        setRanking();

        mAuth = FirebaseAuth.getInstance();

        news_title = new StringBuilder();
        news_link = new StringBuilder();
        listView = (ListView) findViewById(R.id.usernews);
        arrayList = new ArrayList<NewsItem>();
        adapter = new NewsAdapter(this, R.layout.news_item, arrayList);
        listView.setAdapter(adapter);

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        boolean isWhiteListing = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
        }
        if (!isWhiteListing) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        }

        Intent foregroundServiceIntent = new Intent(User_MainActivity.this, UndeadService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(foregroundServiceIntent);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager.cancel(pendingPush);
                rankManager.cancel(pendingRanking);
                mAuth.signOut();
                stopService(foregroundServiceIntent);
                try {
                    ((UndeadService) UndeadService.context_main).onUnbind(serviceIntent);
                    ((User_LocationActivity) User_LocationActivity.context_main).onStop();
                } catch (Exception e) { }

                LoginMaintainService.clearUserName(User_MainActivity.this);
                Toast.makeText(getApplicationContext(), "로그아웃 합니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(User_MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(User_MainActivity.this, User_WalkActivity.class));
                startActivity(new Intent(User_MainActivity.this, User_WalkActivity.class));
                finish();
            }
        });

        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_MainActivity.this, User_BrainMain.class));
                finish();
            }
        });

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_MainActivity.this, User_LocationActivity.class));
                finish();
            }
        });

        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_MainActivity.this, User_RankActivity.class));
                finish();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_MainActivity.this, User_SettingActivity.class));
                finish();
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_MainActivity.this, User_CalendarActivity.class));
                finish();
            }
        });
       mypage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(User_MainActivity.this, User_MypageActivity.class));
               finish();
           }
       });

        //걸음 수 표시
        format = new SimpleDateFormat("yyyybMbd");
        time = Calendar.getInstance();

        format_time = format.format(time.getTime());

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");

        nReference = ref.child("user").child(uid);

        nReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mywalk = (String.valueOf(dataSnapshot.child("walk").child("date").child(format_time).child("walking").getValue()));
                if (mywalk.equals("null")) {
                    walkstep.setText("오늘도 걸어 봅시다!");
                } else {
                    walkstep.setText(mywalk + " 걸음");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "onCancelled", Toast.LENGTH_SHORT);
            }
        });


        nReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mytrain = dataSnapshot.child("today_training").getValue().toString();
                if (mytrain.equals("0")) {
                    brain_train.setText("오늘도 훈련해봐요");
                } else {
                    brain_train.setText(mytrain + " 점");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "onCancelled", Toast.LENGTH_SHORT);
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String link = ((NewsItem) adapter.getItem(i)).getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dementianews.co.kr"+link));
                startActivity(intent);
            }
        });

        NewsAsyncTask newsAsyncTask = new NewsAsyncTask();
        newsAsyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent!=null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }

    private void setPushAlarm() {
        Intent pushIntent = new Intent(User_MainActivity.this, PushAlarmReceiver.class);
        pendingPush = PendingIntent.getBroadcast(User_MainActivity.this, 0, pushIntent, 0);

        Calendar alarm_calendar = Calendar.getInstance();
        alarm_calendar.set(Calendar.HOUR_OF_DAY, 17);
        alarm_calendar.set(Calendar.MINUTE, 59);
        alarm_calendar.set(Calendar.SECOND, 59);

//        alarmManager.set(AlarmManager.RTC, alarm_calendar.getTimeInMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm_calendar.getTimeInMillis(),
                1000 * 60 * 60 * 24, pendingPush);
    }

    private void setRanking() {
        Intent rankingIntent = new Intent(User_MainActivity.this, RankRegisterReceiver.class);
        pendingRanking = PendingIntent.getBroadcast(User_MainActivity.this, 0, rankingIntent, 0);

        Calendar rank_calendar = Calendar.getInstance();
        rank_calendar.set(Calendar.HOUR_OF_DAY, 23);
        rank_calendar.set(Calendar.MINUTE, 59);
        rank_calendar.set(Calendar.SECOND, 59);

//        rankManager.set(AlarmManager.RTC, rank_calendar.getTimeInMillis(), pendingIntent);
        rankManager.setRepeating(AlarmManager.RTC_WAKEUP, rank_calendar.getTimeInMillis(),
                1000 * 60 * 60 * 24, pendingRanking);
    }

    /* 뉴스 크롤링하기 */
    private class NewsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document doc = Jsoup.connect("http://www.dementianews.co.kr/news/articleList.html?view_type=sm").get();
                Element detemeniaNews = doc.select("div.article-list").get(0);
                Elements news = detemeniaNews.select("div.list-titles");

                for(Element e : news){
                    news_title.append( e.text().trim()).append("\n");
                    news_link.append( e.getElementsByAttribute("href").attr("href")).append("\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            final StringTokenizer title = new StringTokenizer(news_title.toString(),"\n"); //뉴스 제목
            final StringTokenizer link = new StringTokenizer(news_link.toString(),"\n");   //뉴스 링크

            arrayList.clear(); // 리스트 초기화

            while(title.hasMoreTokens()) //\n 제거
            {
                String n_title = title.nextToken();
                String n_link = link.nextToken();
                arrayList.add(new NewsItem(n_title,n_link));
            }
            adapter.notifyDataSetChanged();
        }
    }
}