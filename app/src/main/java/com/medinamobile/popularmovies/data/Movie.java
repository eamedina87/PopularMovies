package com.medinamobile.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Erick Medina on 2/5/17.
 */

public class Movie implements Parcelable{

    private int _id;
    private String title;
    private String poster_path;
    private String backdrop_path;
    private String overview;
    private String movie_id;
    private String original_title;
    private String vote_average;
    private String release_date;


    public Movie(){

    }

    public Movie(Parcel in){
        setTitle(in.readString());
        setPoster_path(in.readString());
        setBackdrop_path(in.readString());
        setOverview(in.readString());
        setMovie_id(in.readString());
        setOriginal_title(in.readString());
        setVote_average(in.readString());
        setRelease_date(in.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getTitle());
        parcel.writeString(getPoster_path());
        parcel.writeString(getBackdrop_path());
        parcel.writeString(getOverview());
        parcel.writeString(getMovie_id());
        parcel.writeString(getOriginal_title());
        parcel.writeString(getVote_average());
        parcel.writeString(getRelease_date());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
