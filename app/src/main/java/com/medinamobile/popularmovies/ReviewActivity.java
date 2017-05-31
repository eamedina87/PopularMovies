package com.medinamobile.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.medinamobile.popularmovies.data.Review;
import com.medinamobile.popularmovies.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Supertel on 30/5/17.
 */

public class ReviewActivity extends AppCompatActivity {

    @BindView(R.id.review_content)
    TextView content;
    @BindView(R.id.review_author)
    TextView author;

    private Review review;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (intent!=null && intent.hasExtra(Constants.KEY_REVIEW)){
            this.review = intent.getParcelableExtra(Constants.KEY_REVIEW);
            content.setText(review.getContent());
            author.setText(review.getAuthor());
        }


    }
}
