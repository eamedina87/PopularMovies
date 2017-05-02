package com.medinamobile.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieListener {

    ProgressBar pb_loading;
    TextView tv_error_display;
    RecyclerView recyclerView;
    private MovieAdapter adapter;
    private boolean isPopularSorted;
    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
        tv_error_display = (TextView) findViewById(R.id.tv_error_display);
        recyclerView = (RecyclerView) findViewById(R.id.rv_movies_list);

        String parameter = Utils.PARAMETER_POPULAR;
        String urlString = Utils.getUrlStringSortedBy(parameter);

        if (savedInstanceState==null){
            new loadMoviesFromTMDB().execute(urlString);
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
                    new loadMoviesFromTMDB().execute(urlString);
                    isPopularSorted = true;
                }
                item.setChecked(true);
                return true;
            }
            case R.id.action_sort_top_rated:{
                if (isPopularSorted){
                    String parameter = Utils.PARAMETER_TOP_RATED;
                    String urlString = Utils.getUrlStringSortedBy(parameter);
                    new loadMoviesFromTMDB().execute(urlString);
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

    private class loadMoviesFromTMDB extends AsyncTask<String, Void, ArrayList<Movie>>{

        @Override
        protected void onPreExecute() {
            pb_loading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            ArrayList<Movie> movies = Utils.getMovies(strings[0]);

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            pb_loading.setVisibility(View.INVISIBLE);
            if (movies==null){
                showErrorMessage();
            } else {
                showMoviesList();
                setMoviesForRecyclerView(movies);
            }
            super.onPostExecute(movies);
        }
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
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.movies = savedInstanceState.getParcelableArrayList("movies");
        if (movies!=null){
            setMoviesForRecyclerView(movies);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
