package com.example.logintext.user;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.logintext.R;
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

    private Button logout, walk, training, locate, setting, ranking, calendar;
    private TextView walkstep;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference ref, mReference, nReference;

    private String uid, format_time, mywalk, msg;
    private SimpleDateFormat format;
    private Calendar time;
    private ArrayList<NewsItem> arrayList;
    private NewsAdapter adapter;
    private ListView listView;

    StringBuilder news_title;
    StringBuilder news_link;

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
        walkstep = (TextView) findViewById(R.id.walk_step);
        mAuth = FirebaseAuth.getInstance();

        news_title = new StringBuilder();
        news_link = new StringBuilder();
        listView = (ListView) findViewById(R.id.usernews);
        arrayList = new ArrayList<NewsItem>();
        adapter = new NewsAdapter(this, R.layout.news_item, arrayList);
        listView.setAdapter(adapter);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                LoginMaintainService.clearUserName(User_MainActivity.this);
                Toast.makeText(getApplicationContext(), "로그아웃 합니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(User_MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                startActivity(new Intent(User_MainActivity.this, User_WalkRankActivity.class));
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
                    walkstep.setText(mywalk + "걸음");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "onCancelled", Toast.LENGTH_SHORT);
            }
        });


//    포그라운드 기능
//    private void startForegroundService() {
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        uid = user.getUid();
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("Users");
//        DatabaseReference mReference = ref.child("user").child(uid);
//
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String walked = snapshot.child("walk").getValue().toString();
//
//
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
//                builder.setSmallIcon(R.mipmap.myicon);
//                builder.setContentTitle("77 맞은 어르신");
//                builder.setContentText("현재 걸음수 : "+ walked);
//
//                Intent notificationIntent = new Intent(this, );
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
        //뉴스 링크 타기 에러~~
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String link = ((NewsItem) adapter.getItem(i)).getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);

            }
        });

        NewsAsyncTask newsAsyncTask = new NewsAsyncTask();
        newsAsyncTask.execute();
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

                Document doc = Jsoup.connect("http://www.dementianews.co.kr/news/articleList.html?sc_sub_section_code=S2N2&view_type=sm").get();
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