package com.medinamobile.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.medinamobile.popularmovies.data.Trailer;

import java.util.ArrayList;

/**
 * Created by Supertel on 30/5/17.
 */

public class TrailersFromAPILoader implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>> {

    private Context context;
    private String url;
    private TrailersLoadListener loadListener;

    private ArrayList<Trailer> trailers;

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }

    public interface TrailersLoadListener{
        void onTrailersStartLoading();
        void onTrailersLoaded(ArrayList<Trailer> trailers);
    }

    public TrailersFromAPILoader(Context context, String url, TrailersLoadListener listener){
        this.context = context;
        this.url = url;
        this.loadListener = listener;
    }

    @Override
    public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
        loadListener.onTrailersStartLoading();
        if (trailers==null){
            return new TrailersAsyncTaskLoader(context, url);
        } else{
            return new TrailersAsyncTaskLoader(context, null);
        }

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
        if (data!=null){
            trailers = data;
        } else {
            data = trailers;
        }
        loadListener.onTrailersLoaded(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

    }
}
