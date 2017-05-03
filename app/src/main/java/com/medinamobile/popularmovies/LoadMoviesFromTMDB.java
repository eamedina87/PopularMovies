package com.medinamobile.popularmovies;

import android.os.AsyncTask;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Supertel on 3/5/17.
 */
public class LoadMoviesFromTMDB extends AsyncTask<String, Void, ArrayList<Movie>> {

    private LoadMoviesListener loadMoviesListener;

    public LoadMoviesFromTMDB(LoadMoviesListener listener){
        this.loadMoviesListener = listener;
    }

    public interface LoadMoviesListener{
        void onPreExecute();
        void onPostExecute(ArrayList<Movie> movies);
    }

    @Override
    protected void onPreExecute() {
        loadMoviesListener.onPreExecute();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... strings) {
        ArrayList<Movie> movies = Utils.getMovies(strings[0]);
        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        loadMoviesListener.onPostExecute(movies);
        super.onPostExecute(movies);
    }
}