package com.medinamobile.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Supertel on 30/5/17.
 */

public class Trailer implements Parcelable {

    private String name;
    private String size;
    private String source;
    private String type;

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
        setSource(parcel.readString());
        setType(parcel.readString());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeString(getSize());
        parcel.writeString(getSource());
        parcel.writeString(getType());
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
