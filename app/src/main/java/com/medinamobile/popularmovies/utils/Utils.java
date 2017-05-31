package com.medinamobile.popularmovies.utils;

import android.net.Uri;
import android.os.Bundle;

import com.medinamobile.popularmovies.data.Trailer;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Erick Medina on 2/5/17.
 */

public class Utils {


    public static String getReleaseYear(String release_date) {
        String out = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Constants.dateFormat.parse(release_date));
            out = "("+calendar.get(Calendar.YEAR)+")";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static Bundle createBundle(String url, int index) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_URL, url);
        bundle.putInt(Constants.KEY_SORT_INDEX, index);
        return bundle;
    }

    public static Uri getTrailerUri(Trailer trailer) {
        Uri mUri = Uri.parse(Constants.YOUTUBE_URL_BASE).buildUpon()
                .appendPath(Constants.YOUTUBE_WATCH_PATH)
                .appendQueryParameter(Constants.PARAMETER_YOUTUBE_V, trailer.getKey()).build();

        return mUri;
    }
}
