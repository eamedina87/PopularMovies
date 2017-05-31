package com.medinamobile.popularmovies.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.medinamobile.popularmovies.data.Review;

import java.util.ArrayList;

/**
 * Created by Supertel on 30/5/17.
 */

public class ReviewsFromAPILoader implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

    private final Context context;
    private final String url;

    private ReviewsLoadListener loadListener;
    private ArrayList<Review> reviews;

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public interface ReviewsLoadListener{
        void onReviewsStartLoading();
        void onReviewsLoaded(ArrayList<Review> reviews);
    }

    public ReviewsFromAPILoader(Context context, String url, ReviewsLoadListener listener){
        this.context = context;
        this.url = url;
        this.loadListener = listener;
    }

    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
        loadListener.onReviewsStartLoading();
        if (reviews==null){
            return new ReviewsAsyncTaskLoader(context, url);
        } else {
            return new ReviewsAsyncTaskLoader(context, null);
        }

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
        if (data!=null){
            reviews = data;
        } else {
            data = reviews;
        }
        loadListener.onReviewsLoaded(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {

    }
}
