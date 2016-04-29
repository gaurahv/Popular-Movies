package com.example.gauravagarwal.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GAURAV AGARWAL on 27-04-2016.
 */
public class Movie implements Parcelable {
    String posterUrl;
    String title;
    String overview;
    String releaseDate;
    String user_rating;
    String num_votes;

    public Movie(String posterUrl, String title, String overview, String releaseDate, String user_rating, String num_votes) {
        this.posterUrl = posterUrl;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.user_rating = user_rating;
        this.num_votes = num_votes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(user_rating);
        dest.writeString(num_votes);
    }
    private Movie(Parcel in){
        this.posterUrl = in.readString();
        this.title= in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.user_rating = in.readString();
        this.num_votes = in.readString();

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
