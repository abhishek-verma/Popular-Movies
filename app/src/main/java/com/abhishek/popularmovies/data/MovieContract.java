package com.abhishek.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.abhishek.popularmovies.DiscoverMoviesFragment;

/**
 * Created by Abhishek on 2/15/2016.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.abhishek.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_FAV = "fav";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_MOVIE;

        // Table Name
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVEREAGE = "vote_average";

        public static Uri buildMovieWithIdUri(long id){
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static String getMovieIdFromUri(Uri uri){
            String idStr = uri.getLastPathSegment();
            return idStr;
        }
    }

    public static final class FavEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_MOVIE;

        public static final String TABLE_NAME = "fav";

    }
}
