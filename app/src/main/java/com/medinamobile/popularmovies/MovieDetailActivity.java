package com.medinamobile.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.medinamobile.popularmovies.data.Movie;
import com.medinamobile.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Erick Medina on 2/5/17.
 */


//TODO Add Trailers
//TODO Add Reviews
//TODO Create Styles

public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;
    @BindView(R.id.movie_image)
    ImageView image;
    @BindView(R.id.movie_thumbnail)
    ImageView thumbnail;
    @BindView(R.id.movie_original_title)
    TextView title;
    @BindView(R.id.movie_rating)
    RatingBar rating;
    @BindView(R.id.movie_rating_text)
    TextView rating_text;
    @BindView(R.id.movie_overview)
    TextView overview;
    @BindView(R.id.movie_release_date)
    TextView release_date;
    @BindView(R.id.movie_favorite)
    AppCompatImageButton btnFavorite;

    private boolean isFavorite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra(Intent.EXTRA_CHOSEN_COMPONENT)){
            this.movie = intent.getParcelableExtra(Intent.EXTRA_CHOSEN_COMPONENT);
            populateActivity();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movie!=null)    checkIfIsFavorite();
    }

    private void checkIfIsFavorite() {
        Uri mUri = MovieContract.MovieEntry.FAVORITES_CONTENT_URI.buildUpon().appendPath(movie.getMovie_id()).build();
        Cursor cursor = getContentResolver().query(mUri, null, null, null, null);
        isFavorite = cursor.getCount()>0;
        setFavoriteButtonState(isFavorite);
    }

    private void setFavoriteButtonState(boolean favoriteValue) {
        if (favoriteValue){
            btnFavorite.setSelected(true);
        } else {
            btnFavorite.setSelected(false);
        }
    }

    private void populateActivity() {
        //Load image
        Context context = MovieDetailActivity.this;
        String urlString = Utils.getUrlForMovieImage(movie.getBackdrop_path(), Utils.PARAMETER_SIZE_780);
        Picasso.with(context).load(urlString).into(image);
        urlString = Utils.getUrlForMovieImage(movie.getPoster_path(), Utils.PARAMETER_SIZE_154);
        Picasso.with(context).load(urlString).into(thumbnail);

        //--
        title.setText(movie.getOriginal_title());
        String vote_average = movie.getVote_average();
        int ratingNumber = Math.round(Float.valueOf(vote_average)/2);
        rating.setNumStars(ratingNumber);
        String ratingText = String.format(getString(R.string.rating_text), vote_average);
        rating_text.setText(ratingText);
        overview.setText(movie.getOverview());
        //String releaseString = String.format(getString(R.string.release_date), movie.getRelease_date());
        String releaseString = Utils.getReleaseYear(movie.getRelease_date());
        release_date.setText(releaseString);
    }

    @OnClick(R.id.movie_favorite)
    public void onFavoriteClicked(){

        if (!isFavorite){
            isFavorite = true;
            ContentValues values = getContentValues();
            getContentResolver().insert(
                    MovieContract.MovieEntry.FAVORITES_CONTENT_URI,
                    values);
        } else {
            isFavorite = false;
            Uri mUri = MovieContract.MovieEntry.FAVORITES_CONTENT_URI.buildUpon().appendPath(movie.getMovie_id()).build();
            ContentValues values = getContentValues();
            getContentResolver().delete(
                    mUri,
                    null,
                    null
                    );
        }
        setFavoriteButtonState(isFavorite);
    }

    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, DatabaseUtils.sqlEscapeString(movie.getBackdrop_path()));
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovie_id());
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, DatabaseUtils.sqlEscapeString(movie.getOriginal_title()));
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, DatabaseUtils.sqlEscapeString(movie.getOverview()));
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, DatabaseUtils.sqlEscapeString(movie.getPoster_path()));
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, DatabaseUtils.sqlEscapeString(movie.getTitle()));
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
        return values;
    }




}
