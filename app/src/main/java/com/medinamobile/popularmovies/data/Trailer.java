package com.medinamobile.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Supertel on 30/5/17.
 */

public class Trailer implements Parcelable {

    private String name;
    private String size;
    private String key;
    private String type;
    private String site;

    public static Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Trailer createFromParcel (Parcel parcel){
            return new Trailer(parcel);
        }

        public Trailer[] newArray(int size){
            return new Trailer[size];
        }
    };

    public Trailer(){

    }

    public Trailer(Parcel parcel) {
        setName(parcel.readString());
        setSize(parcel.readString());
        setKey(parcel.readString());
        setType(parcel.readString());
        setSite(parcel.readString());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeString(getSize());
        parcel.writeString(getKey());
        parcel.writeString(getType());
        parcel.writeString(getSite());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
