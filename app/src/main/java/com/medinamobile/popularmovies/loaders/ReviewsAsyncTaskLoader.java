package com.medinamobile.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.medinamobile.popularmovies.utils.APIUtils;
import com.medinamobile.popularmovies.data.Review;

import java.util.ArrayList;

/**
 * Created by Erick Medina on 29/5/17.
 */

public class ReviewsAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Review>> {

    private String reviewsUrl;


    public ReviewsAsyncTaskLoader(Context context, String mUrl) {
        super(context);
        this.reviewsUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (reviewsUrl==null){
            deliverResult(null);
            Log.d("ReviewsAsyncTask","CACHED_RESULT");
        } else {
            forceLoad();
            Log.d("ReviewsAsyncTask","INTERNET_CONNECTION_RESULT");
        }

    }

    @Override
    public ArrayList<Review> loadInBackground() {
        ArrayList<Review> reviews = APIUtils.getReviews(reviewsUrl);
        return reviews;

    }

}
