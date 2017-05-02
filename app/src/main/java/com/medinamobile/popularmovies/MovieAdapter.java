package com.medinamobile.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Supertel on 2/5/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private MovieListener movieListener;
    private ArrayList<Movie> moviesList;

    public interface MovieListener{
        void onMovieClicked(Movie movie);
    }

    public MovieAdapter(ArrayList<Movie> movies, MovieListener movieListener){
        this.moviesList = movies;
        this.movieListener = movieListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        if (holder.title!=null) {
            holder.title.setText(movie.getTitle());
        }
        Context context = holder.itemView.getContext();
        String imageUrl = Utils.getUrlForMovieImage(movie.getPoster_path(), Utils.PARAMETER_SIZE_500);
        Picasso.with(context).load(imageUrl).into(holder.image);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setMoviesList(ArrayList<Movie> movies) {
        moviesList = movies;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView title;
        private int position;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.movie_image);
            title = (TextView) itemView.findViewById(R.id.movie_original_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            movieListener.onMovieClicked(moviesList.get(position));
        }

    }
}
