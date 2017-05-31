package com.medinamobile.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.medinamobile.popularmovies.R;
import com.medinamobile.popularmovies.data.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Supertel on 30/5/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ReviewClickListener reviewClickListener;
    private ArrayList<Review> reviews;

    public interface ReviewClickListener{
        void onReviewClicked(Review review);
    }

    public ReviewAdapter(ArrayList<Review> reviews, ReviewClickListener listener){
        this.reviews = reviews;
        this.reviewClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.position = position;
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_author)
        TextView author;
        @BindView(R.id.review_content)
        TextView content;
        @BindView(R.id.review_button)
        Button button;

        public int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick(R.id.review_button)
        public void onReadMore(){
            reviewClickListener.onReviewClicked(reviews.get(position));
        }
    }


}
