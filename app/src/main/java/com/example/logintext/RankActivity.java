    package com.example.logintext;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class RankActivity extends TabActivity {

        private FirebaseDatabase mDatabase;
        private DatabaseReference mReference;
        private FirebaseUser user;

        private String uid;
        private ListView walk_listView, train_listView;
        ListAdapter walk_adapter, train_adapter;
        List<Walking> WalkingList = new ArrayList<>();
        List<Training> TrainingList = new ArrayList<>();

        class Walking {
            String rankNum, nickname, walked;
            Walking(String rankNum, String nickname, String walked) {
                this.rankNum = rankNum;
                this.nickname = nickname;
                this.walked = walked;
            }
        }

        class Training {
            String rankNum, nickname, trained;
            Training(String rankNum, String nickname, String trained) {
                this.rankNum = rankNum;
                this.nickname = nickname;
                this.trained = trained;
            }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);

        ImageButton back = (ImageButton) findViewById(R.id.before);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RankActivity.this, UserMainActivity.class));
                finish();
            }
        });

        walk_listView = (ListView) findViewById(R.id.walk_item);
        train_listView = (ListView) findViewById(R.id.train_item);
        walk_adapter = new ListAdapter(this);
        train_adapter = new ListAdapter(this);
        walk_listView.setAdapter(walk_adapter);
        train_listView.setAdapter(train_adapter);

        mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
        mReference = mDatabase.getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpecWalk = tabHost.newTabSpec("WALK").setIndicator("걸음수 랭킹");
        tabSpecWalk.setContent(R.id.tabWalk);
        tabHost.addTab(tabSpecWalk);

        TabHost.TabSpec tabSpecTrain = tabHost.newTabSpec("TRAIN").setIndicator("두뇌훈련 랭킹");
        tabSpecTrain.setContent(R.id.tabTrain);
        tabHost.addTab(tabSpecTrain);

        tabHost.setCurrentTab(0);

        // limitToFirst(숫자) 한정 메소드
        mReference.child("user").orderByChild("walk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    int rank = 1;
                    String rankNum = rank+"";
                    String name = messageData.child("name").getValue().toString();
                    String walked = messageData.child("walk").getValue().toString();
                    String trained = messageData.child("type").getValue().toString();
                    WalkingList.add(new Walking(rankNum, name, walked));
                    TrainingList.add(new Training(rankNum, name, trained));
                    rank++;
                }
                walk_adapter.notifyDataSetChanged();
                train_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "onCancelled", Toast.LENGTH_SHORT);
            }
        });
    }

    class ListAdapter extends BaseAdapter {
        Context con;
        ListAdapter (Context con) {
            this.con = con;
        }

        @Override
        public int getCount() {
            return WalkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return WalkingList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.walk_ranking, parent, false);
            }
            ((TextView)convertView.findViewById(R.id.rankNum)).setText(WalkingList.get(position).rankNum);
            ((TextView)convertView.findViewById(R.id.nickname)).setText(WalkingList.get(position).nickname);
            ((TextView)convertView.findViewById(R.id.walked)).setText(WalkingList.get(position).walked);
            return convertView;
        }
    }

//    class TrainingListAdapter extends BaseAdapter {
//        Context con;
//        ListAdapter (Context con) {
//            this.con = con;
//        }
//
//        @Override
//        public int getCount() {
//            return TrainingList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return TrainingList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(
//                        R.layout.train_ranking, parent, false);
//            }
//            ((TextView)convertView.findViewById(R.id.rankNum)).setText(TrainingList.get(position).rankNum);
//            ((TextView)convertView.findViewById(R.id.nickname)).setText(TrainingList.get(position).nickname);
//            ((TextView)convertView.findViewById(R.id.trained)).setText(TrainingList.get(position).trained);
//            return convertView;
//        }
//    }

}
