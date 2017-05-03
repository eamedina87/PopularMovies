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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieListener, LoadMoviesFromTMDB.LoadMoviesListener {

    @BindView(R.id.pb_loading)
    ProgressBar pb_loading;
    @BindView(R.id.tv_error_display)
    TextView tv_error_display;
    @BindView(R.id.rv_movies_list)
    RecyclerView recyclerView;
    private MovieAdapter adapter;
    private boolean isPopularSorted;
    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String parameter = Utils.PARAMETER_POPULAR;
        String urlString = Utils.getUrlStringSortedBy(parameter);

        if (savedInstanceState==null){
            LoadMoviesFromTMDB loadTask = new LoadMoviesFromTMDB(this);
            loadTask.execute(urlString);
            isPopularSorted = true;
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
                if (!isPopularSorted){
                    String parameter = Utils.PARAMETER_POPULAR;
                    String urlString = Utils.getUrlStringSortedBy(parameter);
                    LoadMoviesFromTMDB loadTask = new LoadMoviesFromTMDB(this);
                    loadTask.execute(urlString);
                    isPopularSorted = true;
                }
                item.setChecked(true);
                return true;
            }
            case R.id.action_sort_top_rated:{
                if (isPopularSorted){
                    String parameter = Utils.PARAMETER_TOP_RATED;
                    String urlString = Utils.getUrlStringSortedBy(parameter);
                    LoadMoviesFromTMDB loadTask = new LoadMoviesFromTMDB(this);
                    loadTask.execute(urlString);
                    isPopularSorted = false;
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
        outState.putParcelableArrayList("movies", movies);
        outState.putBoolean("sortCriteria",isPopularSorted);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.movies = savedInstanceState.getParcelableArrayList("movies");
        this.isPopularSorted = savedInstanceState.getBoolean("sortCriteria");
        if (movies!=null){
            setMoviesForRecyclerView(movies);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isPopularSorted){
            menu.findItem(R.id.action_sort_popular).setChecked(true);
        } else {
            menu.findItem(R.id.action_sort_top_rated).setChecked(true);
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
}
