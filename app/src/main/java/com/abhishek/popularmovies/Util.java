package com.abhishek.popularmovies;

import android.content.Context;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abhishek on 21-12-2015.
 */
public class Util {

    public static MovieData[] getWeatherDataFromJSON (String movieDataJSONStr)
            throws JSONException {

        //list of names of JSONObjects to be extracted
        final String TMDB_RESULT_ARRAY = "results";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_ORIG_TITLE = "original_title";
        final String TMDB_RATING = "vote_average";

        JSONObject discoverJSON = new JSONObject(movieDataJSONStr);
        JSONArray resultsArray = discoverJSON.getJSONArray(TMDB_RESULT_ARRAY);

        MovieData[] moviesData = new MovieData[20];
        JSONObject result;
        for(int i=0; i<20; i++){
            result = resultsArray.getJSONObject(i);
            moviesData[i] = new MovieData(
                    "http://image.tmdb.org/t/p/"
                            + "w185"
                            + result.getString(TMDB_POSTER_PATH),//TODO form complete path using URI, and params for size etc
                    result.getString(TMDB_ORIG_TITLE),
                    result.getDouble(TMDB_RATING)
            );
        }
        return moviesData;
    }
}
