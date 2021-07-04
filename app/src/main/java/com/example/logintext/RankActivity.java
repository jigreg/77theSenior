    package com.example.logintext;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RankActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
    }

//    public Query getQuery(DatabaseReference databaseReference, int selectFragment) {
//        Query recentQuery;
//        recentQuery = databaseReference.child("emotion-tag").orderByChild("value");
//    }
}
