package com.example.gauravagarwal.popularmovies;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by GAURAV AGARWAL on 22-05-2016.
 */
public class Utility {

    public final static String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    public final static String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    public final static String API_KEY_PARAM = "api_key";
    private String key;

    public static ArrayList<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException {

        if (movieJsonStr == null) {
            return new ArrayList<Movie>();
        }
        //Main Json object
        JSONObject jsonObject = new JSONObject(movieJsonStr);
        //Resulting array of movies
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        ArrayList<Movie> result = new ArrayList<Movie>();
        for (int i = 0; i < jsonArray.length(); i++) {

            //ith element of array
            JSONObject movie = jsonArray.getJSONObject(i);
            Movie movieObject = getIndivdualMovieDataFromJson(movie);
            result.add(movieObject);
        }
        return result;

    }

    public static ArrayList<Trailer> getTrailerUrlsFromJSON(JSONObject response) throws JSONException {
        if(response == null){
            return  new ArrayList<Trailer>();
        }
        ArrayList<Trailer> trailersList = new ArrayList<Trailer>();
        JSONArray youtubeArray = response.getJSONArray("youtube");
        for (int i = 0; i < youtubeArray.length(); i++) {
            JSONObject trailer = (JSONObject) youtubeArray.get(i);
            String name = trailer.getString("name");
            String src = trailer.getString("source");
            Trailer trailerObject = new Trailer(name, src);
            trailersList.add(trailerObject);
        }
        return trailersList;
    }

    public static ArrayList<Review> getReviewsFromJSON(JSONObject response) throws JSONException {
        if(response == null){
            return  new ArrayList<Review>();
        }
        ArrayList<Review> reviewsList = new ArrayList<Review>();
        JSONArray jsonArray = response.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject review = (JSONObject) jsonArray.get(i);
            String author = review.getString("author");
            String content = review.getString("content");
            Review reviewObject = new Review(content, author);
            reviewsList.add(reviewObject);
        }
        return reviewsList;
    }

    public static String makeUrl(String s, Movie movie, String key) {
        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(movie.id)
                .appendPath(s)
                .appendQueryParameter(API_KEY_PARAM, key)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url.toString();
    }

    public static String makeIndivdualRequestApi(String id, String key) {
        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(id)
                .appendQueryParameter(API_KEY_PARAM, key)
                .build();
        return uri.toString();
    }

    public static Uri makeYoutubeUrl(String s) {
        Uri uri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter("v", s)
                .build();
        return uri;
    }

    public static Movie getIndivdualMovieDataFromJson(JSONObject movie) throws JSONException {
        if(movie == null){
            return new Movie();
        }
        String posterUrl = BASE_IMAGE_URL + movie.getString("poster_path");
        String overview = movie.getString("overview");
        String title = movie.getString("title");
        String user_rating = movie.getString("vote_average");
        String releaseDate = movie.getString("release_date");
        String num_votes = movie.getString("vote_count");
        String id = movie.getString("id");
        Movie movieObject = new Movie(posterUrl, title, overview, releaseDate, user_rating, num_votes, id, false);
        return movieObject;
    }

}

