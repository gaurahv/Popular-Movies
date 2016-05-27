package com.example.gauravagarwal.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gauravagarwal.popularmovies.data.MovieContract;
import com.example.gauravagarwal.popularmovies.data.MovieDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.gauravagarwal.popularmovies.Utility.getMovieDataFromJson;

public class MovieFragment extends Fragment {
    private static final int COLUMN_MOVIE_ID_POSITION = 0;
    private static final int COLUMN_POSTER_URL_POSITION = 1;
    private ImageAdapter imageAdapter;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Default Thumbanil
        Movie sampleThumbanil = new Movie("http://image.tmdb.org/t/p/w185/weUSwMdQIa3NaXVzwUoIIcAi85d.jpg", "Title", "Overview", "Release_date", "User_rating",
                "Num Votes", "id", false
        );

        ArrayList<Movie> list = new ArrayList<Movie>();
        list.add(sampleThumbanil);

        //Setting adapter on list
        imageAdapter = new ImageAdapter(this.getContext(), list);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Grid view From Fragment and setting adapter to it
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(imageAdapter);

        //Setting item listener for details of item ie.movies
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = imageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Getting shared Preference from the settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String SORT_ORDER = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));
        if (SORT_ORDER.equals("My Favorites")) {
            setFavorites();
        } else {
            sendRequest(SORT_ORDER);
        }
    }

    private void setFavorites() {
        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase sqLiteDatabase = movieDbHelper.getReadableDatabase();

        String[] projection = {
                MovieContract.MovieEntry.COLUMN_ID,
                MovieContract.MovieEntry.COLUMN_POSTER_URL
        };

        Cursor c = sqLiteDatabase.query(
                MovieContract.MovieEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                                     // don't group the rows
                null,
                null                            // don't filter by row groups

        );
        c.moveToFirst();
        imageAdapter.clear();
        while (c.moveToNext()) {

            String url = c.getString(COLUMN_POSTER_URL_POSITION);

            Movie movie = new Movie();
            movie.posterUrl = c.getString(COLUMN_POSTER_URL_POSITION);
            movie.id = c.getString(COLUMN_MOVIE_ID_POSITION);
            movie.isFavorite = true;
            imageAdapter.add(movie);
        }
        movieDbHelper.close();
    }

    private void sendRequest(String SORT_ORDER) {
        final String movieJsonStr = null;
        final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
        final String API_KEY_PARAM = "api_key";
        final String key = getString(R.string.APP_KEY);

        if (SORT_ORDER.equals("Most Popular")) {
            SORT_ORDER = "popular";
        } else if (SORT_ORDER.equals("Most Rated")) {
            SORT_ORDER = "top_rated";
        }
        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(SORT_ORDER)
                .appendQueryParameter(API_KEY_PARAM, key)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Movie> result = null;
                        try {
                            result = getMovieDataFromJson(response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        imageAdapter.clear();
                        for (Movie movie : result)
                            imageAdapter.add(movie);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong, please check your internet connection and try again", Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsObjRequest);
    }
}

