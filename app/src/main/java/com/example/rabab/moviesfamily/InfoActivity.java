package com.example.rabab.moviesfamily;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    MoviesFamilyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container, new InfoActivityFragment())
                    .commit();
        }

        setTitle("Detail About Movie");
    }



}
