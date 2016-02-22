package com.abhishek.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.abhishek.popularmovies.data.MovieContract.MovieEntry;
import com.abhishek.popularmovies.data.MovieContract.FavEntry;

/**
 * Created by Abhishek on 2/16/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "weather.db";
    private static final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating Movie Table
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " ( " +
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT UNIQUE NOT NULL, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT UNIQUE NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVEREAGE + " REAL NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL " +
                " )";

        final String SQL_CREATE_FAV_TABLE = "CREATE TABLE " + FavEntry.TABLE_NAME + " ( " +
                FavEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
                " )";

        Log.i(LOG_TAG, "onCreate: SQL Query" + SQL_CREATE_MOVIE_TABLE);
        Log.i(LOG_TAG, "onCreate: SQL Query" + SQL_CREATE_FAV_TABLE);

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
