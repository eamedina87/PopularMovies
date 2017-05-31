package com.medinamobile.popularmovies.utils;

import java.text.SimpleDateFormat;

/**
 * Created by Supertel on 30/5/17.
 */

public class Constants {
    public static final int SORT_POPULAR = 1000;
    public static final int SORT_TOP_RATED = 1001;
    public static final int SORT_FAVORITE = 1002;

    public static final String URL_BASE = "http://api.themoviedb.org/3/movie/";

    public static final String PARAMETER_POPULAR = "popular";
    public static final String PARAMETER_TOP_RATED = "top_rated";
    public static final String PARAMETER_TRAILERS = "videos";
    public static final String PARAMETER_REVIEWS = "reviews";
    public static final String QUERY_API = "api_key";
    /* THE API KEY must be obtained from The Movie Database
         https://www.themoviedb.org */
    public static final String TMDB_API_KEY = "YOUR_API_KEY";

    public static final String IMAGE_URL_BASE = "http://image.tmdb.org/t/p/";
    public static final String PARAMETER_SIZE_92 = "w92";
    public static final String PARAMETER_SIZE_154 = "w154";
    public static final String PARAMETER_SIZE_185 = "w185";
    public static final String PARAMETER_SIZE_342 = "w342";
    public static final String PARAMETER_SIZE_500 = "w500";
    public static final String PARAMETER_SIZE = PARAMETER_SIZE_500;
    public static final String PARAMETER_SIZE_780 = "w780";
    public static final String PARAMETER_SIZE_ORIGINAL = "original";

    public static final String YOUTUBE_URL_BASE = "https://www.youtube.com/";
    public static final String YOUTUBE_WATCH_PATH = "watch";
    public static final String PARAMETER_YOUTUBE_V = "v";

    public static final String API_RESULTS = "results";
    public static final String API_MOVIE_POSTER_PATH = "poster_path";
    public static final String API_MOVIE_BACKDROP_PATH = "backdrop_path";
    public static final String API_MOVIE_OVERVIEW = "overview";
    public static final String API_ID = "id";
    public static final String API_MOVIE_ORIGINAL_TITLE = "original_title";
    public static final String API_MOVIE_TITLE = "title";
    public static final String API_MOVIE_VOTE_AVERAGE = "vote_average";
    public static final String API_MOVIE_RELEASE_DATE = "release_date";

    public static final String API_REVIEW_AUTHOR = "author";
    public static final String API_REVIEW_CONTENT = "content";
    public static final String API_REVIEW_URL = "url";

    public static final String API_TRAILER_RESULTS = "youtube";
    public static final String API_TRAILER_NAME = "name";
    public static final String API_TRAILER_SIZE = "size";
    public static final String API_TRAILER_KEY = "key";
    public static final String API_TRAILER_SITE = "site";
    public static final String API_TRAILER_SITE_YOUTUBE = "YouTube";
    public static final String API_TRAILER_TYPE = "type";
    public static final String API_TRAILER_TYPE_TRAILER = "Trailer";
    public static final String API_TRAILER_TYPE_CLIP = "Clip";
    public static final String API_TRAILER_TYPE_FEATURETTE = "Featurette";

    public static final String API_DATE_FORMAT = "yyyy-MM-dd";

    public static SimpleDateFormat dateFormat = new SimpleDateFormat(API_DATE_FORMAT);

    public static final String KEY_MOVIES = "movies";
    public static final String KEY_MOVIE = "movie";
    public static final String KEY_REVIEWS = "reviews";
    public static final String KEY_TRAILERS = "trailers";
    public static final String KEY_IS_FAVORITE = "isFavorite";
    public static final String KEY_SORT_INDEX = "sortIndex";
    public static final String KEY_URL = "url";
    public static final String KEY_POPULAR_MOVIES = "popular_movies";
    public static final String KEY_TOP_MOVIES = "top_movies";
    public static final String KEY_FAVORITE_MOVIES = "favorite_movies";
    public static final String KEY_REVIEW = "review";

    public static final int ID_MOVIE_FROM_API_LOADER = 600;
    public static final int ID_REVIEW_FROM_API_LOADER = 700;
    public static final int ID_TRAILER_FROM_API_LOADER = 800;



}
