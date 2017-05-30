package com.medinamobile.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.medinamobile.popularmovies.data.Movie;
import com.medinamobile.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Erick Medina on 2/5/17.
 */

public class Utils {

    public static final int SORT_POPULAR = 1000;
    public static final int SORT_TOP_RATED = 1001;
    public static final int SORT_FAVORITE = 1002;
    private static final String URL_BASE = "http://api.themoviedb.org/3/movie/";
    public static final String PARAMETER_POPULAR = "popular";
    public static final String PARAMETER_TOP_RATED = "top_rated";
    private static final String QUERY_API = "api_key";
    /* THE API KEY must be obtained from The Movie Database
     https://www.themoviedb.org */
    private static final String TMDB_API_KEY = "YOUR_API_KEY";

    private static final String IMAGE_URL_BASE = "http://image.tmdb.org/t/p/";
    public static final String PARAMETER_SIZE_92 = "w92";
    public static final String PARAMETER_SIZE_154 = "w154";
    public static final String PARAMETER_SIZE_185 = "w185";
    public static final String PARAMETER_SIZE_342 = "w342";
    public static final String PARAMETER_SIZE_500 = "w500";
    public static final String PARAMETER_SIZE_780 = "w780";
    private static final String PARAMETER_SIZE_ORIGINAL = "original";
    private static final String PARAMETER_SIZE = PARAMETER_SIZE_500;

    private static final String API_RESULTS = "results";
    private static final String API_MOVIE_POSTER_PATH = "poster_path";
    private static final String API_MOVIE_BACKDROP_PATH = "backdrop_path";
    private static final String API_MOVIE_OVERVIEW = "overview";
    private static final String API_MOVIE_ID = "id";
    private static final String API_MOVIE_ORIGINAL_TITLE = "original_title";
    private static final String API_MOVIE_TITLE = "title";
    private static final String API_MOVIE_VOTE_AVERAGE = "vote_average";
    private static final String API_MOVIE_RELEASE_DATE = "release_date";

    public static final String API_DATE_FORMAT = "yyyy-MM-dd";
    public static final String KEY_MOVIES = "movies";
    public static final String KEY_SORT_INDEX = "sortIndex";
    public static final String KEY_URL = "url";
    public static final String KEY_POPULAR_MOVIES = "popular_movies";
    public static final String KEY_TOP_MOVIES = "top_movies";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat(API_DATE_FORMAT);

    public static String getUrlStringSortedBy(String sort){
        Uri mUri =  Uri.parse(URL_BASE).buildUpon()
                .appendPath(sort)
                .appendQueryParameter(QUERY_API,TMDB_API_KEY)
                .build();


        String urlString = null;
        try {
            URL mUrl = new URL(mUri.toString());
            urlString = mUrl.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return urlString;

    }




    public static ArrayList<Movie> getMovies(String urlString){
        ArrayList<Movie> movies = null;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                String stringResponse = response.body().string();
                movies = parseMoviesFromJson(stringResponse);
                Log.i("HTTP-RESPONSE", stringResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }

    private static ArrayList<Movie> parseMoviesFromJson(String stringResponse) {
        ArrayList<Movie> movies = null;
        try {
            JSONObject root = new JSONObject(stringResponse);
            JSONArray results = root.getJSONArray(API_RESULTS);
            if (results!=null && results.length()>0){
                movies = new ArrayList<>();
                for (int cont=0; cont<results.length();cont++){
                    Movie movie = new Movie();
                    JSONObject jsonMovie = results.getJSONObject(cont);
                    movie.setBackdrop_path(jsonMovie.getString(API_MOVIE_BACKDROP_PATH));
                    movie.setMovie_id(jsonMovie.getString(API_MOVIE_ID));
                    movie.setOriginal_title(jsonMovie.getString(API_MOVIE_ORIGINAL_TITLE));
                    movie.setOverview(jsonMovie.getString(API_MOVIE_OVERVIEW));
                    movie.setPoster_path(jsonMovie.getString(API_MOVIE_POSTER_PATH));
                    movie.setRelease_date(jsonMovie.getString(API_MOVIE_RELEASE_DATE));
                    movie.setVote_average(jsonMovie.getString(API_MOVIE_VOTE_AVERAGE));
                    movie.setTitle(jsonMovie.getString(API_MOVIE_TITLE));
                    movies.add(movie);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static String getUrlForMovieImage(String imagePath, String parameterSize) {
        Uri mUri = Uri.parse(IMAGE_URL_BASE).buildUpon()
                .appendPath(parameterSize)
                .appendEncodedPath(imagePath)
                .build();

        String urlString = null;

        try {
            URL mUrl = new URL(mUri.toString());
            urlString = mUrl.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        urlString = urlString.replace("'","");
        return urlString;
    }

    public static Movie getMovieFromCursor(Cursor data) {
        Movie movie = new Movie();
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


    public static String getReleaseYear(String release_date) {
        String out = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(release_date));
            out = "("+calendar.get(Calendar.YEAR)+")";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static Bundle createBundle(String url, int index) {
        Bundle bundle = new Bundle();
        bundle.putString(Utils.KEY_URL, url);
        bundle.putInt(Utils.KEY_SORT_INDEX, index);
        return bundle;
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
