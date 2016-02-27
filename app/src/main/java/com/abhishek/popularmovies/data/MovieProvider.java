package com.abhishek.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.abhishek.popularmovies.data.MovieContract;
/**
 * Created by Abhishek on 2/16/2016.
 */
public class MovieProvider extends ContentProvider {

    public static final String pPopularMoviesSortOrder =
            MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
    public static final String pTopRatedMoviesSortOrder =
            MovieContract.MovieEntry.COLUMN_VOTE_AVEREAGE + " DESC";
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private static final int MOVIES = 100;
    private static final int FAVOURITE_MOVIES = 101;
    private static final int MOVIE_BY_ID = 10;
    // The URI Matcher used by this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder pFavouriteMoviesQueryBuilder;

    static {
        pFavouriteMoviesQueryBuilder = new SQLiteQueryBuilder();

        pFavouriteMoviesQueryBuilder.setTables(
                MovieContract.MovieEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.FavEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        " . " + MovieContract.MovieEntry._ID +
                        " = " + MovieContract.FavEntry.TABLE_NAME +
                        ". " + MovieContract.FavEntry._ID);
    }

    private MovieDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,
                MovieContract.PATH_MOVIE,
                MOVIES);
        matcher.addURI(authority,
                MovieContract.PATH_FAV,
                FAVOURITE_MOVIES);
        matcher.addURI(authority,
                MovieContract.PATH_MOVIE + "/#",
                MOVIE_BY_ID);


        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_BY_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case FAVOURITE_MOVIES:
                return MovieContract.FavEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;

        switch (sUriMatcher.match(uri)){
            case MOVIES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder
                );
                break;
            case FAVOURITE_MOVIES:
                retCursor = pFavouriteMoviesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder);
                break;
            case MOVIE_BY_ID:
                Log.d(LOG_TAG, "query: id = " + MovieContract.MovieEntry.getMovieIdFromUri(uri));
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID + "=?",
                        new String[]{MovieContract.MovieEntry.getMovieIdFromUri(uri)},
                        null, null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if(retCursor!=null) retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                if(_id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieWithIdUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case FAVOURITE_MOVIES: {
                long _id = db.insertWithOnConflict(MovieContract.FavEntry.TABLE_NAME,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE);
                if(_id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieWithIdUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case FAVOURITE_MOVIES:
                rowsDeleted = db.delete(
                        MovieContract.FavEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final  SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;

        switch (match) {
            case MOVIES: {
                db.beginTransaction();
                try {
                    for (ContentValues val : values) {
                        long _id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, null, val, SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            }

            case FAVOURITE_MOVIES: {
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.FavEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            }

            default: {
                return super.bulkInsert(uri, values);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
