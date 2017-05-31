package com.medinamobile.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Supertel on 30/5/17.
 */

public class Review implements Parcelable {

    private String id;
    private String author;
    private String content;
    private String url;


    public Review(){

    }

    public Review(Parcel parcel) {
        setId(parcel.readString());
        setAuthor(parcel.readString());
        setContent(parcel.readString());
        setUrl(parcel.readString());

    }

    public static Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Review createFromParcel(Parcel parcel){
            return new Review(parcel);
        }

        public Review[] newArray (int size){
            return new Review[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getId());
        parcel.writeString(getAuthor());
        parcel.writeString(getContent());
        parcel.writeString(getUrl());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
