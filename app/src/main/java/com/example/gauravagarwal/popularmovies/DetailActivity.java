package com.example.gauravagarwal.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, new DetailFragment())
                    .commit();
        }
    }
}
