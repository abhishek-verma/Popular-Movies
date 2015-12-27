package com.abhishek.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * WIll contain methods for various uses
 * Created by Abhishek on 21-12-2015.
 */
class Util {

    /**
     * Gets movies data in JSON String
     * parses the data to form a meaningful array of type MovieData
     * @param movieDataJSONStr the JSONData it receives
     * @return Array of type MovieData containing meaningful movie data from to JSON provided
     * @throws JSONException
     */
    public static MovieData[] getMoviesDataFromJSON(String movieDataJSONStr)
            throws JSONException {

        //list of names of JSONObjects to be extracted
        final String TMDB_RESULT_ARRAY = "results";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_ORIG_TITLE = "original_title";
        final String TMDB_RATING = "vote_average";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_BACKDROP_PATH = "backdrop_path";
        final String TMDB_RELEASE_DATE = "release_date";

        JSONObject discoverJSON = new JSONObject(movieDataJSONStr);
        JSONArray resultsArray = discoverJSON.getJSONArray(TMDB_RESULT_ARRAY);

        MovieData[] moviesData = new MovieData[20];
        JSONObject result;
        for(int i=0; i<20; i++){
            result = resultsArray.getJSONObject(i);
            //TODO increase poster image sizes
            //coz everyone isn't still stuck with 2G
            moviesData[i] = new MovieData(
                    "http://image.tmdb.org/t/p/"
                            + "w185"
                            + result.getString(TMDB_POSTER_PATH),//TODO form complete path using URI, and params for size etc
                    result.getString(TMDB_ORIG_TITLE),
                    result.getDouble(TMDB_RATING),
                    result.getString(TMDB_OVERVIEW),
                    "http://image.tmdb.org/t/p/"
                            + "w300"
                            + result.getString(TMDB_BACKDROP_PATH),//TODO form complete path using URI, and params for size etc
                    result.getString(TMDB_RELEASE_DATE)
            );
        }
        return moviesData;
    }
}
