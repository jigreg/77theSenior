package com.example.logintext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GPSHistoryActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser user;

    private String uid;
    private ListView listView;
    ListAdapter adapter;
    List<History> historyList = new ArrayList<>();

    class History {
        String time, address, langitude, longitude;
        History(String time, String address, String langitude, String longitude) {
            this.time = time;
            this.address = address;
            this.langitude = langitude;
            this.longitude = longitude;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_history);

        ImageButton back = (ImageButton) findViewById(R.id.before4);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GPSHistoryActivity.this, LocationActivity.class));
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.list_item);
        adapter = new ListAdapter(this);
        listView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance("https://oldman-eb51e-default-rtdb.firebaseio.com/");
        mReference = mDatabase.getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mReference.child("protector").child(uid).child("myUser").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String msg = task.getResult().getValue().toString();

                mReference.child("user").child(msg).child("gps").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                            String time = messageData.child("time").getValue().toString();
                            String add = messageData.child("address").getValue().toString();
                            String lat = messageData.child("latitude").getValue().toString();
                            String lon = messageData.child("longitude").getValue().toString();
                            historyList.add(new History(time, add, lat, lon));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"onCancelled", Toast.LENGTH_SHORT);
                    }
                });
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
            return historyList.size();
        }

        @Override
        public Object getItem(int position) {
            return historyList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.history_item, parent, false);
            }
            ((TextView)convertView.findViewById(R.id.time)).setText(historyList.get(position).time);
            ((TextView)convertView.findViewById(R.id.address)).setText(historyList.get(position).address);
            ((TextView)convertView.findViewById(R.id.latitude)).setText(historyList.get(position).langitude);
            ((TextView)convertView.findViewById(R.id.longitude)).setText(historyList.get(position).longitude);
            return convertView;
        }
    }
}
