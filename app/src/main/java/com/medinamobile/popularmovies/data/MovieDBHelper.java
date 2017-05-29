package com.medinamobile.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Erick Medina on 28/5/17.
 */

public class MovieDBHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "movies.db";
    private static final int DB_VERSION = 2;



    public MovieDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createStatement = "CREATE TABLE "+ MovieContract.MovieEntry.TABLE_NAME+" ("
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.MovieEntry.COLUMN_BACKDROP_PATH +" TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID+" TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE+" TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_OVERVIEW+" TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_POSTER_PATH+" TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_RELEASE_DATE+" TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_TITLE+ " TEXT NOT NULL,"
                + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE+" TEXT NOT NULL,"
                +" UNIQUE ("+ MovieContract.MovieEntry.COLUMN_MOVIE_ID+"));";
        sqLiteDatabase.execSQL(createStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE "+ MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
