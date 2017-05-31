package com.medinamobile.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medinamobile.popularmovies.R;
import com.medinamobile.popularmovies.data.Trailer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Supertel on 30/5/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private final ArrayList<Trailer> trailers;
    private TrailerClickListener trailerClickListener;

    public interface TrailerClickListener{
        void onTrailerClicked(Trailer trailer);
    }

    public TrailerAdapter(ArrayList<Trailer> trailers, TrailerClickListener listener){
        this.trailers = trailers;
        this.trailerClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.position = position;
        holder.name.setText(trailer.getName());
        holder.size.setText(trailer.getSize());
        holder.type.setText(trailer.getType());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_name)
        TextView name;
        @BindView(R.id.trailer_type)
        TextView type;
        @BindView(R.id.trailer_size)
        TextView size;

        public int position;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trailerClickListener.onTrailerClicked(trailers.get(position));
                }
            });
        }
    }


}
