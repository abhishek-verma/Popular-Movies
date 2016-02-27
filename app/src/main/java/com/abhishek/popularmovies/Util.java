package com.abhishek.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.abhishek.popularmovies.data.MovieContract;
import com.abhishek.popularmovies.details.DataObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * WIll contain methods for various uses
 * Created by Abhishek on 21-12-2015.
 */
public class Util {

    private static final String LOG_TAG = Util.class.getSimpleName();

    /**
     * Gets movies data in JSON String
     * parses the data and saves in DB
     * @param movieDataJSONStr the JSONData it receives
     * @throws JSONException
     */
    public static void getMoviesDataFromJSON(String movieDataJSONStr, Context context)
            throws JSONException {

        String title;
        String posterPath, backdropPath;
        long movieId;
        double popularity, rating;
        String overview;
        String releaseDate;


        //list of names of JSONObjects to be extracted
        final String TMDB_RESULT_ARRAY = "results";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_ORIG_TITLE = "original_title";
        final String TMDB_RATING = "vote_average";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_BACKDROP_PATH = "backdrop_path";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_MOVIE_ID = "id";
        final String TMDB_POPULARITY = "popularity";

        JSONObject discoverJSON = new JSONObject(movieDataJSONStr);
        JSONArray resultsArray = discoverJSON.getJSONArray(TMDB_RESULT_ARRAY);

        Vector<ContentValues> cVVector = new Vector<>(resultsArray.length());

        JSONObject result;
        for(int i=0; i<resultsArray.length(); i++){
            result = resultsArray.getJSONObject(i);

            //TODO increase poster and backdrop image sizes
            title = result.getString(TMDB_ORIG_TITLE);
            overview = result.getString(TMDB_OVERVIEW);
            rating = result.getDouble(TMDB_RATING);
            popularity = result.getDouble(TMDB_POPULARITY);
            posterPath = "http://image.tmdb.org/t/p/"
                            + "w185"
                            + result.getString(TMDB_POSTER_PATH);//TODO form complete path using URI, and params for size etc
            backdropPath = "http://image.tmdb.org/t/p/"
                            + "w300"
                            + result.getString(TMDB_BACKDROP_PATH);//TODO form complete path using URI, and params for size etc
            movieId = result.getLong(TMDB_MOVIE_ID);
            releaseDate = result.getString(TMDB_RELEASE_DATE);

            ContentValues movieContentValues = new ContentValues();

            movieContentValues.put(MovieContract.MovieEntry._ID, movieId);
            movieContentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);
            movieContentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, posterPath);
            movieContentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, backdropPath);
            movieContentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVEREAGE, rating);
            movieContentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
            movieContentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
            movieContentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);

            cVVector.add(movieContentValues);
        }

        int inserted = 0;
        //adding to db
        if(cVVector.size()>0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = context.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        }

        Log.d(LOG_TAG, "getMoviesDataFromJSON complete, " + inserted + " inserted");
    }

    public static String FetchDataFromUrl(URL url){

        HttpURLConnection tmdbURLConnection = null;
        BufferedReader reader = null;

        String dataJSONStr = null;

        try {

            //Create a request to TMDB, and open the connection
            tmdbURLConnection = (HttpURLConnection) url.openConnection();
            tmdbURLConnection.connect();

            //Read the input stream into a string
            InputStream inputStream = tmdbURLConnection.getInputStream();
            if(inputStream == null )
                return null;
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder responseStrBuf = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null){
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                responseStrBuf.append(line).append("\n");
            }

            if(responseStrBuf.length() == 0) {
                //response String is empty,
                //No point in parsing
                return null;
            }

            dataJSONStr = responseStrBuf.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error " + e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it.
            return null;
        }
        finally {
            if(tmdbURLConnection!=null)
                tmdbURLConnection.disconnect();

            if(reader != null){
                try{
                    reader.close();
                } catch (IOException e){
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return dataJSONStr;
    }


    public static DataObjects.TrailerData[] parseTrailerDataFromJson(String JSONStr)
            throws JSONException{

        String name;
        String src;

        //list of names of JSONObjects to be extracted
        final String TMDB_YOUTUBE_ARRAY = "youtube";
        final String TMDB_TRAILER_NAME = "name";
        final String TMDB_TRAILER_SRC = "source";

        JSONObject trailerJson = new JSONObject(JSONStr);
        JSONArray youtubeTrailerArray = trailerJson.getJSONArray(TMDB_YOUTUBE_ARRAY);

        DataObjects.TrailerData[] dataArray = new DataObjects.TrailerData[youtubeTrailerArray.length()];

        for (int i = 0; i < youtubeTrailerArray.length(); i++) {
            name = youtubeTrailerArray.getJSONObject(i).getString(TMDB_TRAILER_NAME);
            src = youtubeTrailerArray.getJSONObject(i).getString(TMDB_TRAILER_SRC);

            dataArray[i] = new DataObjects.TrailerData(name, src);
        }

        return dataArray;
    }

    public static DataObjects.ReviewData[] parseReviewDataFromJson(String JSONStr)
            throws JSONException{

        String name;
        String content;

        //list of names of JSONObjects to be extracted
        final String TMDB_RESULTS_ARRAY = "results";
        final String TMDB_AUTHOR_NAME = "author";
        final String TMDB_REVIEW_CONTENT = "content";

        JSONObject reviewJson = new JSONObject(JSONStr);
        JSONArray resultArray = reviewJson.getJSONArray(TMDB_RESULTS_ARRAY);

        DataObjects.ReviewData[] dataArray = new DataObjects.ReviewData[resultArray.length()];

        for (int i = 0; i < resultArray.length(); i++) {
            name = resultArray.getJSONObject(i).getString(TMDB_AUTHOR_NAME);
            content = resultArray.getJSONObject(i).getString(TMDB_REVIEW_CONTENT);

            dataArray[i] = new DataObjects.ReviewData(name, content);
        }

        return dataArray;
    }
}
