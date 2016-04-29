package com.example.gauravagarwal.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class MovieFragment extends Fragment {

    private ImageAdapter imageAdapter;
    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Default Thumbanil
        Movie sampleThumbanil = new Movie("http://image.tmdb.org/t/p/w185/weUSwMdQIa3NaXVzwUoIIcAi85d.jpg", "Title", "Overview", "Release_date", "User_rating",
                "Num Votes"
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
        String sortOrder = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));

        //Getting movie data from website
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute(sortOrder);
    }

    private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException {

        //Main Json object
        JSONObject jsonObject = new JSONObject(movieJsonStr);
        //Resulting array of movies
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        ArrayList<Movie> result = new ArrayList<Movie>();
        final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
        for (int i = 0; i < jsonArray.length(); i++) {
            String posterUrl;
            String title;
            String overview;
            String releaseDate;
            String user_rating;
            String num_votes;
            //ith element of array
            JSONObject movie = jsonArray.getJSONObject(i);

            posterUrl = BASE_IMAGE_URL + movie.getString("poster_path");
            overview = movie.getString("overview");
            title = movie.getString("title");
            user_rating = movie.getString("vote_average");
            releaseDate = movie.getString("release_date");
            num_votes = movie.getString("vote_count");
            Movie movieObject = new Movie(posterUrl, title, overview, releaseDate, user_rating, num_votes);
            result.add(movieObject);
        }
        return result;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if (params == null) {
                return null;
            }


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonStr = null;
            String SORT_ORDER = "";
            final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
            final String API_KEY_PARAM = "api_key";
            final String key = getString(R.string.APP_KEY);

            if (params[0].equals("Most Popular")) {
                SORT_ORDER = "popular";
            } else if (params[0].equals("Most Rated")) {
                SORT_ORDER = "top_rated";
            } else {
                return null;
            }
            try {
                Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(SORT_ORDER)
                        .appendQueryParameter(API_KEY_PARAM, key)
                        .build();
                URL url = new URL(uri.toString());

                // Create the request to movie database, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if (result != null) {
                imageAdapter.clear();
                for(Movie movie: result)
                    imageAdapter.add(movie);
            }
        }
    }
}

