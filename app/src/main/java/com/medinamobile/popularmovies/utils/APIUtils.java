package com.medinamobile.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.medinamobile.popularmovies.data.Movie;
import com.medinamobile.popularmovies.data.Review;
import com.medinamobile.popularmovies.data.Trailer;

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

public class APIUtils {
    public static String getUrlStringSortedBy(String sort){
        Uri mUri =  Uri.parse(Constants.URL_BASE).buildUpon()
                .appendPath(sort)
                .appendQueryParameter(Constants.QUERY_API, Constants.TMDB_API_KEY)
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

    public static String getUrlStringTrailers(String id){
        Uri mUri =  Uri.parse(Constants.URL_BASE).buildUpon()
                .appendPath(id)
                .appendPath(Constants.PARAMETER_TRAILERS)
                .appendQueryParameter(Constants.QUERY_API, Constants.TMDB_API_KEY)
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

    public static String getUrlStringReviews(String id){
        Uri mUri =  Uri.parse(Constants.URL_BASE).buildUpon()
                .appendPath(id)
                .appendPath(Constants.PARAMETER_REVIEWS)
                .appendQueryParameter(Constants.QUERY_API, Constants.TMDB_API_KEY)
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
                movies = JsonUtils.parseMoviesFromJson(stringResponse);
                //Log.d("HTTP-RESPONSE", stringResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public static String getUrlForMovieImage(String imagePath, String parameterSize) {
        Uri mUri = Uri.parse(Constants.IMAGE_URL_BASE).buildUpon()
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

    public static ArrayList<Trailer> getTrailers(String urlString) {
        ArrayList<Trailer> trailers = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                String stringResponse = response.body().string();
                trailers = JsonUtils.parseTrailersFromJson(stringResponse);
                //Log.d("HTTP-RESPONSE", stringResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return trailers;
    }

    public static ArrayList<Review> getReviews(String urlString) {
        ArrayList<Review> reviews = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                String stringResponse = response.body().string();
                reviews = JsonUtils.parseReviewsFromJson(stringResponse);
                Log.d("APIUtils.getReviews", stringResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return reviews;
    }
}
