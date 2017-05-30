package com.medinamobile.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.medinamobile.popularmovies.Utils;
import com.medinamobile.popularmovies.data.Movie;

import java.util.ArrayList;

/**
 * Created by Erick Medina on 29/5/17.
 */

public class MoviesFromAPILoader implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>{

    public static final int ID_MOVIE_FROM_API_LOADER = 600;
    private Context context;
    private int sortType;

    private MoviesLoaderCallbacks moviesLoaderCallbacks;
    private ArrayList<Movie> popularMovies;
    private ArrayList<Movie> topMovies;

    public interface MoviesLoaderCallbacks{
        void onPreExecute();
        void onPostExecute(ArrayList<Movie> movies);
    }

    public MoviesFromAPILoader(Context context, MoviesLoaderCallbacks callbacks){
        this.context = context;
        this.moviesLoaderCallbacks = callbacks;
    }

    public void setMovies(ArrayList<Movie> popular_movies, ArrayList<Movie> top_movies) {
        popularMovies = popular_movies;
        topMovies = top_movies;
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        moviesLoaderCallbacks.onPreExecute();
        sortType = args.getInt(Utils.KEY_SORT_INDEX);
        String moviesUrl = null;
        if (sortType==Utils.SORT_POPULAR && popularMovies==null
                || sortType==Utils.SORT_TOP_RATED && topMovies==null ){
            moviesUrl = args.getString(Utils.KEY_URL);
        }
        return new MovieAsyncTaskLoader(context, moviesUrl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        if (data!=null) {
            if (sortType == Utils.SORT_POPULAR){
                popularMovies = data;
            } else if (sortType == Utils.SORT_TOP_RATED){
                topMovies = data;
            }
        }else {
            if (sortType == Utils.SORT_POPULAR){
                data = popularMovies;
            } else if (sortType == Utils.SORT_TOP_RATED){
                data = topMovies;
            }
        }
        moviesLoaderCallbacks.onPostExecute(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        moviesLoaderCallbacks = null;
    }


}
