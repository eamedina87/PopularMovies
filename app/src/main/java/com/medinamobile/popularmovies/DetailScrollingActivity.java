package com.medinamobile.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.medinamobile.popularmovies.data.Movie;
import com.medinamobile.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailScrollingActivity extends AppCompatActivity {

    @BindView(R.id.movie_image)
    ImageView image;
    @BindView(R.id.movie_favorite)
    FloatingActionButton btnFavorite;
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

    private Movie movie;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_scrolling);
        ButterKnife.bind(this);

        setupActionBar();

        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra(Intent.EXTRA_CHOSEN_COMPONENT)){
            this.movie = intent.getParcelableExtra(Intent.EXTRA_CHOSEN_COMPONENT);
            populateActivity();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movie!=null){
            isFavorite = Utils.isMovieFavorited(this, movie.getMovie_id());
            setFavoriteButtonState(isFavorite);
        }
    }


    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateActivity() {
        //Load image
        Context context = DetailScrollingActivity.this;
        String urlString = Utils.getUrlForMovieImage(movie.getBackdrop_path(), Utils.PARAMETER_SIZE_780);
        Picasso.with(context).load(urlString).into(image);
        urlString = Utils.getUrlForMovieImage(movie.getPoster_path(), Utils.PARAMETER_SIZE_154);
        Picasso.with(context).load(urlString).into(thumbnail);

        //--
        getSupportActionBar().setTitle(movie.getTitle());
        String vote_average = movie.getVote_average();
        int ratingNumber = Math.round(Float.valueOf(vote_average)/2);
        rating.setNumStars(ratingNumber);
        String ratingText = String.format(getString(R.string.rating_text), vote_average);
        rating_text.setText(ratingText);
        String releaseString = Utils.getReleaseYear(movie.getRelease_date());
        release_date.setText(releaseString);
        title.setText(movie.getOriginal_title());
        overview.setText(movie.getOverview());

    }

    @OnClick(R.id.movie_favorite)
    public void onFavoriteClicked(){

        if (!isFavorite){
            isFavorite = true;
            ContentValues values = Utils.getContentValues(movie);
            getContentResolver().insert(
                    MovieContract.MovieEntry.FAVORITES_CONTENT_URI,
                    values);
        } else {
            isFavorite = false;
            Uri mUri = MovieContract.MovieEntry.FAVORITES_CONTENT_URI.buildUpon().appendPath(movie.getMovie_id()).build();
            getContentResolver().delete(
                    mUri,
                    null,
                    null
            );
        }
        setFavoriteButtonState(isFavorite);
    }

    private void setFavoriteButtonState(boolean favoriteValue) {
        if (favoriteValue){
            btnFavorite.setImageResource(R.drawable.ic_favorite_selected);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_unselected);
        }
    }



}
