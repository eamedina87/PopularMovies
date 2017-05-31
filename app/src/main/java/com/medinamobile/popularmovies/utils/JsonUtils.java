package com.medinamobile.popularmovies.utils;

import com.medinamobile.popularmovies.data.Movie;
import com.medinamobile.popularmovies.data.Review;
import com.medinamobile.popularmovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Supertel on 30/5/17.
 */

public class JsonUtils {

    public static ArrayList<Movie> parseMoviesFromJson(String stringResponse) {
        ArrayList<Movie> movies = null;
        try {
            JSONObject root = new JSONObject(stringResponse);
            JSONArray results = root.getJSONArray(Constants.API_RESULTS);
            if (results!=null && results.length()>0){
                movies = new ArrayList<>();
                for (int cont=0; cont<results.length();cont++){
                    Movie movie = new Movie();
                    JSONObject jsonMovie = results.getJSONObject(cont);
                    movie.setBackdrop_path(jsonMovie.getString(Constants.API_MOVIE_BACKDROP_PATH));
                    movie.setMovie_id(jsonMovie.getString(Constants.API_ID));
                    movie.setOriginal_title(jsonMovie.getString(Constants.API_MOVIE_ORIGINAL_TITLE));
                    movie.setOverview(jsonMovie.getString(Constants.API_MOVIE_OVERVIEW));
                    movie.setPoster_path(jsonMovie.getString(Constants.API_MOVIE_POSTER_PATH));
                    movie.setRelease_date(jsonMovie.getString(Constants.API_MOVIE_RELEASE_DATE));
                    movie.setVote_average(jsonMovie.getString(Constants.API_MOVIE_VOTE_AVERAGE));
                    movie.setTitle(jsonMovie.getString(Constants.API_MOVIE_TITLE));
                    movies.add(movie);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static ArrayList<Review> parseReviewsFromJson(String stringResponse) {
        ArrayList<Review> reviews = null;
        try {
            JSONObject root = new JSONObject(stringResponse);
            JSONArray results = root.getJSONArray(Constants.API_RESULTS);
            if (results!=null && results.length()>0){
                reviews = new ArrayList<>();
                for (int cont=0; cont<results.length();cont++){
                    Review review = new Review();
                    JSONObject jsonReview = results.getJSONObject(cont);
                    review.setUrl(jsonReview.getString(Constants.API_REVIEW_URL));
                    review.setId(jsonReview.getString(Constants.API_ID));
                    review.setContent(jsonReview.getString(Constants.API_REVIEW_CONTENT));
                    review.setAuthor(jsonReview.getString(Constants.API_REVIEW_AUTHOR));
                    reviews.add(review);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public static ArrayList<Trailer> parseTrailersFromJson(String stringResponse) {
        ArrayList<Trailer> trailers = null;
        try {
            JSONObject root = new JSONObject(stringResponse);
            JSONArray results = root.getJSONArray(Constants.API_TRAILER_RESULTS);
            if (results!=null && results.length()>0){
                trailers = new ArrayList<>();
                for (int cont=0; cont<results.length();cont++){
                    Trailer trailer = new Trailer();
                    JSONObject jsonReview = results.getJSONObject(cont);
                    trailer.setType(jsonReview.getString(Constants.API_TRAILER_TYPE));
                    trailer.setSource(jsonReview.getString(Constants.API_TRAILER_SOURCE));
                    trailer.setSize(jsonReview.getString(Constants.API_TRAILER_SIZE));
                    trailer.setName(jsonReview.getString(Constants.API_TRAILER_NAME));
                    if (trailer.getType().equals(Constants.API_TRAILER_TYPE_TRAILER))  trailers.add(trailer);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailers;
    }

}
