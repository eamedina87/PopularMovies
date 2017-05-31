package com.medinamobile.popularmovies.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.medinamobile.popularmovies.utils.DBUtils;
import com.medinamobile.popularmovies.data.Movie;
import com.medinamobile.popularmovies.data.MovieContract;

import java.util.ArrayList;

/**
 * Created by Erick Medina on 28/5/17.
 */

public class FavoriteLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int ID_FAVORITES_LOADER = 400;
    public static final int ID_FAVORITE_BY_ID_LOADER = 500;

    public static final String KEY_ID = "movie_id";
    private Context context;

    private FavoriteCallbacks favoriteListener;

    public interface FavoriteCallbacks{
        void onFavoritesReady(ArrayList<Movie> movies);
        void onFavoriteMovieReady(Movie movie);
    }

    public FavoriteLoader(Context context, FavoriteCallbacks listener){
        this.context = context;
        this.favoriteListener = listener;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(FavoriteLoader.class.getName(), "OnCreateLoader");
        switch (id){
            case ID_FAVORITES_LOADER:{
                Uri favoritesUri = MovieContract.MovieEntry.FAVORITES_CONTENT_URI;
                String sortBy = MovieContract.MovieEntry.COLUMN_RELEASE_DATE+" ASC";
                return new CursorLoader(this.context,
                        favoritesUri,
                        null,
                        null,
                        null,
                        sortBy);
            }
            case ID_FAVORITE_BY_ID_LOADER:{
                if (args.containsKey(KEY_ID)){
                    String movieId = args.getString(KEY_ID);
                    String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?";
                    Uri favoriteByIdUri = MovieContract.MovieEntry.FAVORITES_CONTENT_URI.buildUpon().appendPath(movieId).build();
                    return new CursorLoader(this.context,
                            favoriteByIdUri,
                            null,
                            selection,
                            new String[]{movieId},
                            null);
                }

            }
            default:
                throw new UnsupportedOperationException("Loader not implemented");
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(FavoriteLoader.class.getName(), "OnLoadFinished");
        Log.d(FavoriteLoader.class.getName(), "dataPos:" + data.getPosition());
        if (data.getCount()==1){
            Movie movie = DBUtils.getMovieFromCursor(data);
            favoriteListener.onFavoriteMovieReady(movie);
        } else {
            ArrayList<Movie> movies = DBUtils.getMoviesFromCursor(data);
            favoriteListener.onFavoritesReady(movies);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }



}
