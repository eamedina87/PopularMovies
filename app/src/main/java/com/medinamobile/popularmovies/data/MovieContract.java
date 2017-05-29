package com.medinamobile.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Erick Medina on 28/5/17.
 */

public class MovieContract {


    public static final String AUTHORITY = "com.medinamobile.popularmovies.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class MovieEntry implements BaseColumns{

        public static final Uri FAVORITES_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE =  "original_title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE =  "vote_average";
        public static final String COLUMN_TITLE = "title";


    }

}
