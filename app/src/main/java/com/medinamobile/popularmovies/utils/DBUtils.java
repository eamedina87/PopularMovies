package com.medinamobile.popularmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;

import com.medinamobile.popularmovies.data.Movie;
import com.medinamobile.popularmovies.data.MovieContract;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Supertel on 30/5/17.
 */

public class DBUtils {
    public static Movie getMovieFromCursor(Cursor data) {
        Movie movie = new Movie();
        if (data.getPosition()==-1) data.moveToNext();
        movie.setRelease_date(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
        movie.setVote_average(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
        movie.setOriginal_title(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)).replace("'",""));
        movie.set_id(data.getInt(data.getColumnIndex(MovieContract.MovieEntry._ID)));
        movie.setBackdrop_path(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH)));
        movie.setPoster_path(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
        movie.setTitle(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)).replace("'",""));
        movie.setMovie_id(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
        movie.setOverview(data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)).replace("'",""));
        return movie;
    }

    public static ArrayList<Movie> getMoviesFromCursor(Cursor data) {
        ArrayList<Movie> movies = new ArrayList<>();
        while (data.moveToNext()){
            Movie movie = getMovieFromCursor(data);
            movies.add(movie);
        }
        return movies;
    }

    public static ContentValues getContentValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, DatabaseUtils.sqlEscapeString(movie.getBackdrop_path()));
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovie_id());
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, DatabaseUtils.sqlEscapeString(movie.getOriginal_title()));
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, DatabaseUtils.sqlEscapeString(movie.getOverview()));
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, DatabaseUtils.sqlEscapeString(movie.getPoster_path()));
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, DatabaseUtils.sqlEscapeString(movie.getTitle()));
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
        return values;
    }

    public static boolean isMovieFavorited(Context context, String movieId) {
        Uri mUri = MovieContract.MovieEntry.FAVORITES_CONTENT_URI.buildUpon().appendPath(movieId).build();
        Cursor cursor = context.getContentResolver().query(mUri, null, null, null, null);
        return cursor.getCount()>0;

    }

}
