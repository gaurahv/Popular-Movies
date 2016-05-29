package com.example.gauravagarwal.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity implements MovieFragment.Callback {
    boolean mTwopane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null) {
            mTwopane = true;
            if (savedInstanceState == null) {
                onItemSelected(0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(int position) {

        GridView gridView = (GridView) findViewById(R.id.gridview);
        Movie movie = (Movie) gridView.getItemAtPosition(position);

        if (movie != null) {
            if (mTwopane) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("MOVIE", movie);
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, detailFragment).commit();
            } else {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movie);
                startActivity(intent);
            }
        }
    }
}
