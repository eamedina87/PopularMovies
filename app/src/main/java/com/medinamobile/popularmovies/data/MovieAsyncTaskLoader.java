package com.medinamobile.popularmovies.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.medinamobile.popularmovies.Utils;

import java.util.ArrayList;

/**
 * Created by Erick Medina on 29/5/17.
 */

public class MovieAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    private String moviesUrl;


    public MovieAsyncTaskLoader(Context context, String mUrl) {
        super(context);
        this.moviesUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        if (moviesUrl == null || moviesUrl.isEmpty()){
            Log.d("MovieAsyncTaskLoader", "CACHED_RESULT");
            return null;

        } else {
            ArrayList<Movie> movies = Utils.getMovies(moviesUrl);
            Log.d("MovieAsyncTaskLoader", "INTERNET_CONNECTION_RESULT");
            return movies;
        }
    }

}
