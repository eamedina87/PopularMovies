package com.medinamobile.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.medinamobile.popularmovies.adapters.ReviewAdapter;
import com.medinamobile.popularmovies.adapters.TrailerAdapter;
import com.medinamobile.popularmovies.data.Movie;
import com.medinamobile.popularmovies.data.MovieContract;
import com.medinamobile.popularmovies.data.Review;
import com.medinamobile.popularmovies.data.Trailer;
import com.medinamobile.popularmovies.loaders.ReviewsFromAPILoader;
import com.medinamobile.popularmovies.loaders.TrailersFromAPILoader;
import com.medinamobile.popularmovies.utils.APIUtils;
import com.medinamobile.popularmovies.utils.Constants;
import com.medinamobile.popularmovies.utils.DBUtils;
import com.medinamobile.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//(TODO) Use cached information of reviews and trailer on orientation changes
public class DetailScrollingActivity extends AppCompatActivity implements ReviewsFromAPILoader.ReviewsLoadListener, TrailersFromAPILoader.TrailersLoadListener, ReviewAdapter.ReviewClickListener, TrailerAdapter.TrailerClickListener {

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
    @BindView(R.id.reviews_label)
    TextView reviews_label;
    @BindView(R.id.trailers_label)
    TextView trailers_label;
    @BindView(R.id.movie_reviews)
    RecyclerView rv_reviews;
    @BindView(R.id.movie_trailers)
    RecyclerView rv_trailers;

    ProgressBar progressBarReviews;
    ProgressBar progressBarTrailers;

    private Movie movie;
    private ArrayList<Review> reviews;
    private ArrayList<Trailer> trailers;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_scrolling);
        ButterKnife.bind(this);

        setupActionBar();

        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra(Constants.KEY_MOVIE)){
            this.movie = intent.getParcelableExtra(Constants.KEY_MOVIE);
            populateActivity();
            createTrailersLoaderCallbacks();
            createReviewsLoaderCallbacks();
        }
    }

    private void createReviewsLoaderCallbacks() {
        String url = APIUtils.getUrlStringReviews(movie.getMovie_id());
        ReviewsFromAPILoader loader = new ReviewsFromAPILoader(this, url, this);
        loader.setReviews(reviews);
        getSupportLoaderManager().initLoader(
                Constants.ID_REVIEW_FROM_API_LOADER,
                null,
                loader
                );
    }

    private void createTrailersLoaderCallbacks() {
        String url = APIUtils.getUrlStringTrailers(movie.getMovie_id());
        TrailersFromAPILoader loader = new TrailersFromAPILoader(this, url, this);
        loader.setTrailers(trailers);
        getSupportLoaderManager().initLoader(
                Constants.ID_TRAILER_FROM_API_LOADER,
                null,
                loader
        );

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movie!=null){
            isFavorite = DBUtils.isMovieFavorited(this, movie.getMovie_id());
            setFavoriteButtonState(isFavorite);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.KEY_REVIEWS, reviews);
        outState.putParcelableArrayList(Constants.KEY_TRAILERS, trailers);
        outState.putParcelable(Constants.KEY_MOVIE, movie);
        outState.putBoolean(Constants.KEY_IS_FAVORITE, isFavorite);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        reviews = savedInstanceState.getParcelableArrayList(Constants.KEY_REVIEWS);
        trailers = savedInstanceState.getParcelableArrayList(Constants.KEY_TRAILERS);
        movie = savedInstanceState.getParcelable(Constants.KEY_MOVIE);
        isFavorite = savedInstanceState.getBoolean(Constants.KEY_IS_FAVORITE);
        createReviewsLoaderCallbacks();
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateActivity() {
        //Load image
        Context context = DetailScrollingActivity.this;
        String urlString = APIUtils.getUrlForMovieImage(movie.getBackdrop_path(), Constants.PARAMETER_SIZE_780);
        Picasso.with(context).load(urlString).into(image);
        urlString = APIUtils.getUrlForMovieImage(movie.getPoster_path(), Constants.PARAMETER_SIZE_154);
        Picasso.with(context).load(urlString).into(thumbnail);
        //--
        getSupportActionBar().setTitle(movie.getTitle());
        String vote_average = movie.getVote_average();
        //int ratingNumber = Math.round(Float.valueOf(vote_average)/2);
        rating.setRating(Float.valueOf(vote_average)/2);
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
            ContentValues values = DBUtils.getContentValues(movie);
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

    @Override
    public void onTrailersLoaded(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        if (trailers!=null && trailers.size()>0){
            rv_trailers.setVisibility(View.VISIBLE);
            trailers_label.setVisibility(View.VISIBLE);
            setupTrailersList(trailers);

        }
//        progressBarTrailers.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onReviewsLoaded(ArrayList<Review> reviews) {
        this.reviews = reviews;
        if (reviews!=null && reviews.size()>0) {
            rv_reviews.setVisibility(View.VISIBLE);
            reviews_label.setVisibility(View.VISIBLE);
            setupReviewsList(reviews);
        }
//        progressBarReviews.setVisibility(View.INVISIBLE);
    }

    private void setupTrailersList(ArrayList<Trailer> trailers) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        rv_trailers.setLayoutManager(manager);
        TrailerAdapter adapter = new TrailerAdapter(trailers, this);
        rv_trailers.setAdapter(adapter);
    }

    private void setupReviewsList(ArrayList<Review> reviews) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rv_reviews.setLayoutManager(manager);
        ReviewAdapter adapter = new ReviewAdapter(reviews, this);
        rv_reviews.setAdapter(adapter);
    }

    @Override
    public void onReviewClicked(Review review) {
        //Read More clicked
        //Open Review Activity
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra(Constants.KEY_REVIEW, review);
        startActivity(intent);

    }

    @Override
    public void onTrailerClicked(Trailer trailer) {
        Uri mUri = Utils.getTrailerUri(trailer);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(mUri);
        startActivity(intent);
    }

    @Override
    public void onReviewsStartLoading() {
//        progressBarReviews.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrailersStartLoading() {
//        progressBarTrailers.setVisibility(View.VISIBLE);
    }
}
