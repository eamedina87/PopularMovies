package com.medinamobile.popularmovies;

import android.content.Intent;
import android.os.Parcelable;
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

import com.medinamobile.popularmovies.adapters.MovieAdapter;
import com.medinamobile.popularmovies.loaders.FavoriteLoader;
import com.medinamobile.popularmovies.data.Movie;
import com.medinamobile.popularmovies.loaders.MoviesFromAPILoader;
import com.medinamobile.popularmovies.utils.APIUtils;
import com.medinamobile.popularmovies.utils.Constants;
import com.medinamobile.popularmovies.utils.Utils;

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
    private ArrayList<Movie> favorite_movies;
    private String urlString;
    private Menu menu;
    private GridLayoutManager layoutManager;
    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState==null){
            String parameter = Constants.PARAMETER_POPULAR;
            urlString = APIUtils.getUrlStringSortedBy(parameter);
            sortIndex = Constants.SORT_POPULAR;
            createMovieLoaderCallbacks();
        }

        layoutManager = new GridLayoutManager(MainActivity.this,
                2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.listState!=null){
            layoutManager.onRestoreInstanceState(listState);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(Constants.ID_MOVIE_FROM_API_LOADER);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.KEY_MOVIES, movies);
        outState.putParcelableArrayList(Constants.KEY_POPULAR_MOVIES, popular_movies);
        outState.putParcelableArrayList(Constants.KEY_TOP_MOVIES, top_movies);
        outState.putParcelableArrayList(Constants.KEY_FAVORITE_MOVIES, favorite_movies);
        outState.putInt(Constants.KEY_SORT_INDEX,sortIndex);
        outState.putString(Constants.KEY_URL, urlString);
        listState = layoutManager.onSaveInstanceState();
        outState.putParcelable(Constants.KEY_LIST_STATE, listState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.movies = savedInstanceState.getParcelableArrayList(Constants.KEY_MOVIES);
        this.popular_movies = savedInstanceState.getParcelableArrayList(Constants.KEY_POPULAR_MOVIES);
        this.top_movies = savedInstanceState.getParcelableArrayList(Constants.KEY_TOP_MOVIES);
        this.favorite_movies = savedInstanceState.getParcelableArrayList(Constants.KEY_FAVORITE_MOVIES);
        this.sortIndex = savedInstanceState.getInt(Constants.KEY_SORT_INDEX);
        this.urlString = savedInstanceState.getString(Constants.KEY_URL);
        this.listState = savedInstanceState.getParcelable(Constants.KEY_LIST_STATE);
        createMovieLoaderCallbacks();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (sortIndex== Constants.SORT_POPULAR){
            menu.findItem(R.id.action_sort_popular).setChecked(true);
        } else if (sortIndex== Constants.SORT_TOP_RATED){
            menu.findItem(R.id.action_sort_top_rated).setChecked(true);
        } else if (sortIndex== Constants.SORT_FAVORITE){
            menu.findItem(R.id.action_sort_favorite).setChecked(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_sort_popular:{
                if (sortIndex!= Constants.SORT_POPULAR){
                    sortIndex = Constants.SORT_POPULAR;
                    String parameter = Constants.PARAMETER_POPULAR;
                    urlString = APIUtils.getUrlStringSortedBy(parameter);
                    getSupportLoaderManager().restartLoader(
                            Constants.ID_MOVIE_FROM_API_LOADER,
                            Utils.createBundle(urlString, sortIndex),
                            loaderCallbacks);
                    destroyFavoritesLoader();
                }
                item.setChecked(true);
                setRefreshVisibility();
                return true;
            }
            case R.id.action_sort_top_rated:{
                if (sortIndex!= Constants.SORT_TOP_RATED){
                    sortIndex = Constants.SORT_TOP_RATED;
                    String parameter = Constants.PARAMETER_TOP_RATED;
                    urlString = APIUtils.getUrlStringSortedBy(parameter);
                    getSupportLoaderManager().restartLoader(
                            Constants.ID_MOVIE_FROM_API_LOADER,
                            Utils.createBundle(urlString, sortIndex),
                            loaderCallbacks);
                    destroyFavoritesLoader();
                }
                item.setChecked(true);
                setRefreshVisibility();
                return true;
            }
            case R.id.action_sort_favorite:{
                if (sortIndex!= Constants.SORT_FAVORITE){
                    sortIndex = Constants.SORT_FAVORITE;
                    getSupportLoaderManager().initLoader(
                            FavoriteLoader.ID_FAVORITES_LOADER,
                            null,
                            new FavoriteLoader(this, this));
                }
                item.setChecked(true);
                setRefreshVisibility();
                return true;
            }
            case R.id.action_refresh:{
                if (sortIndex!=Constants.SORT_FAVORITE){
                    loaderCallbacks.setMovies(null, null, favorite_movies);
                    recyclerView.removeAllViewsInLayout();
                    getSupportLoaderManager().restartLoader(
                            Constants.ID_MOVIE_FROM_API_LOADER,
                            Utils.createBundle(urlString, sortIndex),
                            loaderCallbacks);
                    setRefreshVisibility();
                    return  true;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setRefreshVisibility() {
        if (sortIndex==Constants.SORT_FAVORITE){
            menu.findItem(R.id.action_refresh).setVisible(false);
        } else {
            menu.findItem(R.id.action_refresh).setVisible(true);
        }
    }


    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailScrollingActivity.class);
        intent.putExtra(Constants.KEY_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public void onPreExecute() {
        pb_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(ArrayList<Movie> movies, int sortType) {
        pb_loading.setVisibility(View.INVISIBLE);
        if (movies==null){
            showErrorMessage();
        } else {
            showMoviesList();
            setMoviesForRecyclerView(movies, sortType);
        }
    }

    @Override
    public void onFavoritesReady(ArrayList<Movie> movies) {
        if (Constants.SORT_FAVORITE == sortIndex) setMoviesForRecyclerView(movies, Constants.SORT_FAVORITE);
    }

    @Override
    public void onFavoriteMovieReady(Movie movie) {
        //No need to implement it in this Main Activity
    }

    private void destroyFavoritesLoader() {
        getSupportLoaderManager().destroyLoader(FavoriteLoader.ID_FAVORITES_LOADER);
    }

    private void createMovieLoaderCallbacks() {

        loaderCallbacks = new MoviesFromAPILoader(this, this);
        loaderCallbacks.setMovies(popular_movies, top_movies, favorite_movies);
        getSupportLoaderManager().initLoader(
                Constants.ID_MOVIE_FROM_API_LOADER,
                Utils.createBundle(urlString, sortIndex),
                loaderCallbacks);
        /*
        if (sortIndex==Constants.SORT_FAVORITE) {
            setMoviesForRecyclerView(movies, sortIndex);
        }
        */
    }

    private void setMoviesForRecyclerView(ArrayList<Movie> movies, int sortType) {
        if (sortType==sortIndex) {
            //sortType==sortIndex avoids MovieLoaderCallbacks asignation when navigating in favorite movies

            if (sortIndex == Constants.SORT_POPULAR) {
                popular_movies = movies;
                this.movies = movies;
            } else if (sortIndex == Constants.SORT_TOP_RATED) {
                top_movies = movies;
                this.movies = movies;
            } else if (sortIndex == Constants.SORT_FAVORITE) {
                favorite_movies = movies;
                this.movies = movies;
            }
        }

        if (adapter==null){
            adapter = new MovieAdapter(this.movies, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setMoviesList(this.movies);
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


}
