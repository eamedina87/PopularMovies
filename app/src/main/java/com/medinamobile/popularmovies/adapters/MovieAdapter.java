package com.medinamobile.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.medinamobile.popularmovies.R;
import com.medinamobile.popularmovies.data.Movie;
import com.medinamobile.popularmovies.utils.APIUtils;
import com.medinamobile.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erick Medina on 2/5/17.
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
        String imageUrl = APIUtils.getUrlForMovieImage(movie.getPoster_path(), Constants.PARAMETER_SIZE_500);
        //boolean isFavorite = Utils.isMovieFavorited(movie.)
        Picasso.with(context).load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(holder.image);
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

        @BindView(R.id.movie_image)
        ImageView image;
        @Nullable @BindView(R.id.movie_favorite)
        ImageView favorite;
        @Nullable @BindView(R.id.movie_original_title)
        TextView title;

        private int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            movieListener.onMovieClicked(moviesList.get(position));
        }

    }
}
