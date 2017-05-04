package com.medinamobile.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Supertel on 2/5/17.
 */

public class Utils {

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
                    movie.setId(jsonMovie.getString(API_MOVIE_ID));
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

        return urlString;
    }
}
