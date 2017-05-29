package com.medinamobile.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.medinamobile.popularmovies.data.MovieContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



//LoaderCallbacks are for Favorite Movies Callbacks
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieListener,
        LoadMoviesFromTMDB.LoadMoviesListener, FavoriteLoader.FavoriteCallbacks{


    @BindView(R.id.pb_loading)
    ProgressBar pb_loading;
    @BindView(R.id.tv_error_display)
    TextView tv_error_display;
    @BindView(R.id.rv_movies_list)
    RecyclerView recyclerView;

    private static final int SORT_POPULAR = 1000;
    private static final int SORT_TOP_RATED = 1001;
    private static final int SORT_FAVORITE = 1002;

    private static final String KEY_MOVIES = "movies";
    private static final String KEY_SORT_INDEX = "sortIndex";

    private MovieAdapter adapter;
    private int sortIndex;
    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String parameter = Utils.PARAMETER_POPULAR;
        String urlString = Utils.getUrlStringSortedBy(parameter);

        if (savedInstanceState==null){
            //TODO Call AsyncTaskLoader
            LoadMoviesFromTMDB loadTask = new LoadMoviesFromTMDB(this);
            loadTask.execute(urlString);
            sortIndex = SORT_POPULAR;
        }

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
                if (sortIndex!=SORT_POPULAR){
                    sortIndex = SORT_POPULAR;
                    String parameter = Utils.PARAMETER_POPULAR;
                    String urlString = Utils.getUrlStringSortedBy(parameter);
                    LoadMoviesFromTMDB loadTask = new LoadMoviesFromTMDB(this);
                    loadTask.execute(urlString);

                }
                item.setChecked(true);
                return true;
            }
            case R.id.action_sort_top_rated:{
                if (sortIndex!=SORT_TOP_RATED){
                    sortIndex = SORT_TOP_RATED;
                    String parameter = Utils.PARAMETER_TOP_RATED;
                    String urlString = Utils.getUrlStringSortedBy(parameter);
                    LoadMoviesFromTMDB loadTask = new LoadMoviesFromTMDB(this);
                    loadTask.execute(urlString);

                }
                item.setChecked(true);
                return true;
            }
            case R.id.action_sort_favorite:{
                if (sortIndex!=SORT_FAVORITE){
                    sortIndex = SORT_FAVORITE;
                    FavoriteLoader loader = new FavoriteLoader(this, this);
                    getSupportLoaderManager().initLoader(FavoriteLoader.ID_FAVORITES_LOADER, null, loader);

                }
                item.setChecked(true);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.putExtra(Intent.EXTRA_CHOSEN_COMPONENT, movie);
        startActivity(intent);
    }



    private void setMoviesForRecyclerView(ArrayList<Movie> movies) {
        this.movies = movies;
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
        outState.putParcelableArrayList(KEY_MOVIES, movies);
        outState.putInt(KEY_SORT_INDEX,sortIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.movies = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
        this.sortIndex = savedInstanceState.getInt(KEY_SORT_INDEX);
        if (movies!=null){
            setMoviesForRecyclerView(movies);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (sortIndex==SORT_POPULAR){
            menu.findItem(R.id.action_sort_popular).setChecked(true);
        } else if (sortIndex==SORT_TOP_RATED){
            menu.findItem(R.id.action_sort_top_rated).setChecked(true);
        } else if (sortIndex==SORT_FAVORITE){
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
        if (SORT_FAVORITE == sortIndex) setMoviesForRecyclerView(movies);
    }

    @Override
    public void onFavoriteMovieReady(Movie movie) {
        //No need to implement it in this Main Activity
    }
}
