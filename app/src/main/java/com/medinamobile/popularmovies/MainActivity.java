package com.medinamobile.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.medinamobile.popularmovies.data.FavoriteLoader;
import com.medinamobile.popularmovies.data.Movie;
import com.medinamobile.popularmovies.data.MoviesFromAPILoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieListener,
         FavoriteLoader.FavoriteCallbacks, MoviesFromAPILoader.MoviesLoaderCallbacks {


    @BindView(R.id.pb_loading)
    ProgressBar pb_loading;
    @BindView(R.id.tv_error_display)
    TextView tv_error_display;
    @BindView(R.id.rv_movies_list)
    RecyclerView recyclerView;

    private MovieAdapter adapter;
    private int sortIndex;
    private ArrayList<Movie> movies;
    private MoviesFromAPILoader loaderCallbacks;
    private ArrayList<Movie> popular_movies;
    private ArrayList<Movie> top_movies;
    private String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        if (savedInstanceState==null){
            String parameter = Utils.PARAMETER_POPULAR;
            urlString = Utils.getUrlStringSortedBy(parameter);
            sortIndex = Utils.SORT_POPULAR;
            loaderCallbacks = new MoviesFromAPILoader(this, this);

            getSupportLoaderManager().initLoader(
                    MoviesFromAPILoader.ID_MOVIE_FROM_API_LOADER,
                    Utils.createBundle(urlString, sortIndex),
                    loaderCallbacks);
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(MoviesFromAPILoader.ID_MOVIE_FROM_API_LOADER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_sort_popular:{
                if (sortIndex!= Utils.SORT_POPULAR){
                    sortIndex = Utils.SORT_POPULAR;
                    String parameter = Utils.PARAMETER_POPULAR;
                    urlString = Utils.getUrlStringSortedBy(parameter);
                    getSupportLoaderManager().restartLoader(
                            MoviesFromAPILoader.ID_MOVIE_FROM_API_LOADER,
                            Utils.createBundle(urlString, sortIndex),
                            loaderCallbacks);
                    destroyFavoritesLoader();
                }
                item.setChecked(true);
                return true;
            }
            case R.id.action_sort_top_rated:{
                if (sortIndex!= Utils.SORT_TOP_RATED){
                    sortIndex = Utils.SORT_TOP_RATED;
                    String parameter = Utils.PARAMETER_TOP_RATED;
                    urlString = Utils.getUrlStringSortedBy(parameter);
                    getSupportLoaderManager().restartLoader(
                            MoviesFromAPILoader.ID_MOVIE_FROM_API_LOADER,
                            Utils.createBundle(urlString, sortIndex),
                            loaderCallbacks);
                    destroyFavoritesLoader();
                }
                item.setChecked(true);
                return true;
            }
            case R.id.action_sort_favorite:{
                if (sortIndex!= Utils.SORT_FAVORITE){
                    sortIndex = Utils.SORT_FAVORITE;
                    getSupportLoaderManager().initLoader(
                            FavoriteLoader.ID_FAVORITES_LOADER,
                            null,
                            new FavoriteLoader(this, this));
                }
                item.setChecked(true);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void destroyFavoritesLoader() {
        getSupportLoaderManager().destroyLoader(FavoriteLoader.ID_FAVORITES_LOADER);
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_CHOSEN_COMPONENT, movie);
        startActivity(intent);
    }



    private void setMoviesForRecyclerView(ArrayList<Movie> movies) {
        this.movies = movies;
        if (sortIndex==Utils.SORT_POPULAR){
            popular_movies = movies;
        } else if (sortIndex == Utils.SORT_TOP_RATED){
            top_movies = movies;
        }

        if (adapter==null){
            GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this,
                    2, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MovieAdapter(movies, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setMoviesList(movies);
            adapter.notifyDataSetChanged();
        }

    }

    private void showMoviesList() {
        tv_error_display.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        tv_error_display.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Utils.KEY_MOVIES, movies);
        outState.putParcelableArrayList(Utils.KEY_POPULAR_MOVIES, popular_movies);
        outState.putParcelableArrayList(Utils.KEY_TOP_MOVIES, top_movies);
        outState.putInt(Utils.KEY_SORT_INDEX,sortIndex);
        outState.putString(Utils.KEY_URL, urlString);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.movies = savedInstanceState.getParcelableArrayList(Utils.KEY_MOVIES);
        this.popular_movies = savedInstanceState.getParcelableArrayList(Utils.KEY_POPULAR_MOVIES);
        this.top_movies = savedInstanceState.getParcelableArrayList(Utils.KEY_TOP_MOVIES);
        this.sortIndex = savedInstanceState.getInt(Utils.KEY_SORT_INDEX);
        this.urlString = savedInstanceState.getString(Utils.KEY_URL);
        loaderCallbacks = new MoviesFromAPILoader(this, this);
        loaderCallbacks.setMovies(popular_movies, top_movies);
        getSupportLoaderManager().initLoader(
                MoviesFromAPILoader.ID_MOVIE_FROM_API_LOADER,
                Utils.createBundle(urlString, sortIndex),
                loaderCallbacks);

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (sortIndex== Utils.SORT_POPULAR){
            menu.findItem(R.id.action_sort_popular).setChecked(true);
        } else if (sortIndex== Utils.SORT_TOP_RATED){
            menu.findItem(R.id.action_sort_top_rated).setChecked(true);
        } else if (sortIndex== Utils.SORT_FAVORITE){
            menu.findItem(R.id.action_sort_favorite).setChecked(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onPreExecute() {
        pb_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(ArrayList<Movie> movies) {
        pb_loading.setVisibility(View.INVISIBLE);
        if (movies==null){
            showErrorMessage();
        } else {
            showMoviesList();
            setMoviesForRecyclerView(movies);
        }
    }

    @Override
    public void onFavoritesReady(ArrayList<Movie> movies) {
        if (Utils.SORT_FAVORITE == sortIndex) setMoviesForRecyclerView(movies);
    }

    @Override
    public void onFavoriteMovieReady(Movie movie) {
        //No need to implement it in this Main Activity
    }
}
