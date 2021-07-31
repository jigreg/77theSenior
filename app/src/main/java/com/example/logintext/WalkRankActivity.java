package com.example.logintext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("deprecation")
public class WalkRankActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser user;

    private Button trainRank;
    private TextView myRankNum, myNickname, myWalked;

    private int myRank;
    private String uid;
    private ListView walk_listView;
    ListAdapter walk_adapter;
    List<Walking> TempList = new ArrayList<>();
    List<Walking> WalkingList = new ArrayList<>();

    class Walking {
        String nickname, walked;
        Walking(String nickname, String walked) {
            this.nickname = nickname;
            this.walked = walked;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_ranking);

        ImageButton back = (ImageButton) findViewById(R.id.before);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WalkRankActivity.this, UserMainActivity.class));
                finish();
            }
        });

        trainRank = (Button) findViewById(R.id.trainRank);
        myRankNum = (TextView) findViewById(R.id.myRankNum);
        myNickname = (TextView) findViewById(R.id.myNickname);
        myWalked = (TextView) findViewById(R.id.myWalked);

        trainRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WalkRankActivity.this, TrainRankActivity.class));
                finish();
            }
        });

        walk_listView = (ListView) findViewById(R.id.walk_item);
        walk_adapter = new ListAdapter(this);
        walk_listView.setAdapter(walk_adapter);

        mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
        mReference = mDatabase.getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        // limitToFirst(숫자) 한정 메소드
        mReference.child("user").orderByChild("walk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int rank = 1;
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String myUid = messageData.child("uid").getValue().toString();
                    String name = messageData.child("name").getValue().toString();
                    String walked = messageData.child("walk").getValue().toString();

                    TempList.add(new Walking(name, walked));

                    if (myUid.equals(uid)) {
                        myRank = rank;
                    }
                    rank++;
                }
                int size = TempList.size()-1;
                for (int i = 0; i <= size; i++) {
                    Walking temp = TempList.get(size-i);
                    WalkingList.add(i, temp);
                }
                walk_adapter.notifyDataSetChanged();

                myRankNum.setText((WalkingList.size()-myRank+1)+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "onCancelled", Toast.LENGTH_SHORT);
            }
        });

        mReference.child("user").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String walked = dataSnapshot.child("walk").getValue().toString();
                myNickname.setText(name);
                myWalked.setText(walked);
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
                        R.layout.walk_ranking_item, parent, false);
            }
            ((TextView)convertView.findViewById(R.id.rankNum)).setText((position+1)+"");
            ((TextView)convertView.findViewById(R.id.nickname)).setText(WalkingList.get(position).nickname);
            ((TextView)convertView.findViewById(R.id.walked)).setText(WalkingList.get(position).walked);
            return convertView;
        }
    }
}
