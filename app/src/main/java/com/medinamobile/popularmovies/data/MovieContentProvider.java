package com.medinamobile.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Erick Medina on 28/5/17.
 */

public class MovieContentProvider extends ContentProvider {


    public static final int CODE_FAVORITES = 100;
    public static final int CODE_FAVORITES_WITH_ID = 101;

    public UriMatcher mUriMatcher =  buildUriMatcher();

    private MovieDBHelper dbHelper;


    public static final UriMatcher buildUriMatcher (){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITES, CODE_FAVORITES);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAVORITES+"/#", CODE_FAVORITES_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {

        int matchCode = mUriMatcher.match(uri);
        Cursor cursor = null;
        SQLiteDatabase mDb = dbHelper.getReadableDatabase();

        switch (matchCode){
            case CODE_FAVORITES:{

                cursor = mDb.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderBy);
                break;
            }
            case CODE_FAVORITES_WITH_ID:{
                String id = uri.getPathSegments().get(1);
                cursor = mDb.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?",
                        new String[]{id},
                        null,
                        null,
                        orderBy);
                break;
            }
            default:
                throw new IllegalArgumentException("Uri not supported: "+uri.toString());
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri returnUri = null;
        int matchCode = mUriMatcher.match(uri);
        SQLiteDatabase mDb = dbHelper.getWritableDatabase();
        switch (matchCode){
            case CODE_FAVORITES:{
                int rowId = (int) mDb.insert(MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        contentValues);

                if (rowId>0){
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.FAVORITES_CONTENT_URI, rowId);
                } else {
                    throw new RuntimeException("Insert failed with uri: "+uri.toString());
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Uri unknown: "+uri.toString());
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int returnInt = -1;
        int matchCode = mUriMatcher.match(uri);
        SQLiteDatabase mDb = dbHelper.getWritableDatabase();
        switch (matchCode){
            case CODE_FAVORITES:{
                returnInt = mDb.delete(MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        null);
                break;
            }
            case CODE_FAVORITES_WITH_ID:{
                String id = uri.getPathSegments().get(1);
                returnInt = mDb.delete(MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID+"=?",
                        new String[]{id});
                break;
            }
            default:
                throw new UnsupportedOperationException("Uri unknown: "+uri.toString());
        }
        if (returnInt>0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return returnInt;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        return -1;
    }
}
