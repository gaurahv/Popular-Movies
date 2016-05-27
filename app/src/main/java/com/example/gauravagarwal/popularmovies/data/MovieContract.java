package com.example.gauravagarwal.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by GAURAV AGARWAL on 26-05-2016.
 */
public class MovieContract {

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POSTER_URL = "posterurl";
    }
}
