package com.example.gauravagarwal.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gauravagarwal.popularmovies.data.MovieContract.MovieEntry;
import com.example.gauravagarwal.popularmovies.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.gauravagarwal.popularmovies.Utility.getIndivdualMovieDataFromJson;
import static com.example.gauravagarwal.popularmovies.Utility.getReviewsFromJSON;
import static com.example.gauravagarwal.popularmovies.Utility.getTrailerUrlsFromJSON;
import static com.example.gauravagarwal.popularmovies.Utility.makeIndivdualRequestApi;
import static com.example.gauravagarwal.popularmovies.Utility.makeUrl;
import static com.example.gauravagarwal.popularmovies.Utility.makeYoutubeUrl;

/**
 * Created by GAURAV AGARWAL on 26-05-2016.
 */

public class DetailFragment extends Fragment {

    RecyclerView reviewRecyclerView, trailerRecyclerView;
    RecyclerView.LayoutManager trailerLayoutManager;
    LinearLayoutManager reviewLayoutManager;
    private static final int favoriteTag = 1;

    TextView title, description, releaseDate, voteAverage, voteCount;

    ImageView posterImage;
    ImageButton favorite;
    Movie movie;
    private String key;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        key = getString(R.string.APP_KEY);

        trailerRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailer_list);
        reviewRecyclerView = (RecyclerView) rootView.findViewById(R.id.review_list);
        title = (TextView) rootView.findViewById(R.id.title_text_view);
        description = (TextView) rootView.findViewById(R.id.description_text_view);
        releaseDate = (TextView) rootView.findViewById(R.id.release_date_text_view);
        voteAverage = (TextView) rootView.findViewById(R.id.vote_average_text_view);
        voteCount = (TextView) rootView.findViewById(R.id.vote_count_text_view);
        posterImage = (ImageView) rootView.findViewById(R.id.poster_image);
        favorite = (ImageButton) rootView.findViewById(R.id.imageButton);

        // The detail Activity called via intent.  Inspect the intent for  data.
        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            movie = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }
        if(getArguments()!=null) {
            movie = getArguments().getParcelable("MOVIE");
        }
        if(movie!=null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            if (movie.title.equals("Title")) {
                callIndividualApi(movie.id, requestQueue);
            } else {
                setAttributes(movie);
            }
            isMovieFavorite(movie.id);
            setFavoriteImageSource(movie.isFavorite);

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDatabase();
                }
            });
            setTrailers(movie, requestQueue);
            setReviews(movie, requestQueue);
        }
        return rootView;
    }

    private void updateDatabase() {
        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase sqLiteDatabase = movieDbHelper.getReadableDatabase();
        if (movie.isFavorite) {
            String[] projection = {
                    MovieEntry.COLUMN_ID,
            };

            String selection = MovieEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = {
                    movie.id
            };

            sqLiteDatabase.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieEntry.COLUMN_ID, movie.id);
            contentValues.put(MovieEntry.COLUMN_POSTER_URL, movie.posterUrl);
            sqLiteDatabase.insert(MovieEntry.TABLE_NAME, null, contentValues);
        }
        movieDbHelper.close();
        movie.isFavorite = !movie.isFavorite;
        setFavoriteImageSource(movie.isFavorite);
    }

    private void setFavoriteImageSource(boolean isMovieFavorite) {
        if (isMovieFavorite) {
            favorite.setImageResource(R.drawable.favorite);
        } else {
            favorite.setImageResource(R.drawable.unfavorite);
        }
    }

    private void callIndividualApi(String id, RequestQueue requestQueue) {
        String url = makeIndivdualRequestApi(id, key);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            movie = getIndivdualMovieDataFromJson(response);
                            setAttributes(movie);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getContext(), "Something went wrong, please check your internet connection and try again", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsObjRequest);
    }

    private void setTrailers(Movie movie, RequestQueue requestQueue) {
        String url = makeUrl("trailers", movie, key);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setTrailersOnList(getTrailerUrlsFromJSON(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong, please check your internet connection and try again", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsObjRequest);
    }

    private void setReviews(Movie movie, RequestQueue requestQueue) {
        String url = makeUrl("reviews", movie, key);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setReviewsOnList(getReviewsFromJSON(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getContext(), "Something went wrong, please check your internet connection and try again", Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsObjRequest);
    }

    private void setAttributes(Movie movie) {
        title.setText(movie.title);
        Picasso.with(getContext()).load(movie.posterUrl).placeholder(R.drawable.placeholder).into(posterImage);
        releaseDate.setText(movie.releaseDate);
        voteAverage.setText(movie.user_rating + "/10");
        voteCount.setText(movie.num_votes + " votes");
        description.setText(movie.overview);
    }

    private void setReviewsOnList(ArrayList<Review> reviewsFromJSON) {
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviewsFromJSON);
        reviewAdapter.notifyDataSetChanged();
        reviewLayoutManager = new LinearLayoutManager(getContext());
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setAdapter(reviewAdapter);
    }

    private void setTrailersOnList(final ArrayList<Trailer> trailerUrls) {
        final TrailerAdapter trailerAdapter = new TrailerAdapter(trailerUrls);
        trailerAdapter.notifyDataSetChanged();
        trailerLayoutManager = new LinearLayoutManager(getContext());
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);
        trailerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trailerRecyclerView.setAdapter(trailerAdapter);
        trailerRecyclerView.addOnItemTouchListener(new TrailerTouchListener(getContext(), trailerRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Trailer trailer = trailerUrls.get(position);
                Uri uri = makeYoutubeUrl(trailer.getSrc());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }));
    }


    public void isMovieFavorite(String id) {
        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase sqLiteDatabase = movieDbHelper.getReadableDatabase();

        String[] projection = {
                MovieEntry.COLUMN_ID,
        };

        String selection = MovieEntry.COLUMN_ID + "= ?";
        String selectArgs[] = {
                movie.id
        };
        Cursor c = sqLiteDatabase.query(
                MovieEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectArgs,                   // The values for the WHERE clause
                null,                                     // don't group the rows
                null,
                null                            // don't filter by row groups
        );
        if (c.getCount() == 1) {
            movie.isFavorite = true;
        } else {
            movie.isFavorite = false;
        }
        movieDbHelper.close();
    }

}