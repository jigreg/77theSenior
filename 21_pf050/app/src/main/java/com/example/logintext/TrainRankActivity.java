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

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class TrainRankActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser user;

    private Button walkRank;
    private TextView myRankNum, myNickname, myTrained;

    private int myRank;
    private String uid;
    private ListView train_listView;
    ListAdapter train_adapter;
    List<Training> TempList = new ArrayList<>();
    List<Training> TrainingList = new ArrayList<>();

    class Training {
        String nickname, trained;
        Training(String nickname, String trained) {
            this.nickname = nickname;
            this.trained = trained;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.train_ranking);

        ImageButton back = (ImageButton) findViewById(R.id.before);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrainRankActivity.this, UserMainActivity.class));
                finish();
            }
        });

        walkRank = (Button) findViewById(R.id.walkRank);
        myRankNum = (TextView) findViewById(R.id.myRankNum);
        myNickname = (TextView) findViewById(R.id.myNickname);
        myTrained = (TextView) findViewById(R.id.myTrained);

        walkRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrainRankActivity.this, WalkRankActivity.class));
                finish();
            }
        });

        train_listView = (ListView) findViewById(R.id.train_item);
        train_adapter = new ListAdapter(this);
        train_listView.setAdapter(train_adapter);

        mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
        mReference = mDatabase.getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        // limitToFirst(숫자) 한정 메소드
        mReference.child("user").orderByChild("birth").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int rank = 1;
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String myUid = messageData.child("uid").getValue().toString();
                    String name = messageData.child("name").getValue().toString();
                    String trained = messageData.child("birth").getValue().toString();

                    TempList.add(new Training(name, trained));

                    if (myUid.equals(uid)) {
                        myRank = rank;
                    }
                    rank++;
                }
                int size = TempList.size()-1;
                for (int i = 0; i <= size; i++) {
                    Training temp = TempList.get(size-i);
                    TrainingList.add(i, temp);
                }
                train_adapter.notifyDataSetChanged();

                myRankNum.setText((TrainingList.size()-myRank+1)+"");
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
                String trained = dataSnapshot.child("birth").getValue().toString();
                myNickname.setText(name);
                myTrained.setText(trained);
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
            return TrainingList.size();
        }

        @Override
        public Object getItem(int position) {
            return TrainingList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.train_ranking_item, parent, false);
            }
            ((TextView)convertView.findViewById(R.id.rankNum)).setText((position+1)+"");
            ((TextView)convertView.findViewById(R.id.nickname)).setText(TrainingList.get(position).nickname);
            ((TextView)convertView.findViewById(R.id.trained)).setText(TrainingList.get(position).trained);
            return convertView;
        }
    }
}
