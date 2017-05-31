package com.medinamobile.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.medinamobile.popularmovies.utils.APIUtils;
import com.medinamobile.popularmovies.data.Trailer;

import java.util.ArrayList;

/**
 * Created by Erick Medina on 29/5/17.
 */

public class TrailersAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Trailer>> {

    private String urlString;


    public TrailersAsyncTaskLoader(Context context, String url) {
        super(context);
        this.urlString = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (urlString==null){
            deliverResult(null);
            Log.d("TrailersAsyncTask","CACHED_RESULT");
        } else {
            forceLoad();
            Log.d("TrailersAsyncTask","INTERNET_CONNECTION_RESULT");
        }

    }

    @Override
    public ArrayList<Trailer> loadInBackground() {
        ArrayList<Trailer> trailers = APIUtils.getTrailers(urlString);
        return trailers;

    }

}
