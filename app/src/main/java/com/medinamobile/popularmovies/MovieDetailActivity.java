package com.medinamobile.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Supertel on 2/5/17.
 */

public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;
    @BindView(R.id.movie_image)
    ImageView image;
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

    private void populateActivity() {
        //Load image
        Context context = MovieDetailActivity.this;
        String urlString = Utils.getUrlForMovieImage(movie.getBackdrop_path(), Utils.PARAMETER_SIZE_780);
        Picasso.with(context).load(urlString).into(image);
        //--
        title.setText(movie.getOriginal_title());
        String vote_average = movie.getVote_average();
        int ratingNumber = Math.round(Float.valueOf(vote_average)/2);
        rating.setNumStars(ratingNumber);
        String ratingText = String.format(getString(R.string.rating_text), vote_average);
        rating_text.setText(ratingText);
        overview.setText(movie.getOverview());
        String releaseString = String.format(getString(R.string.release_date), movie.getRelease_date());
        release_date.setText(releaseString);
    }
}
