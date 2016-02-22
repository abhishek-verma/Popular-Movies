package com.abhishek.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * AsyncTask to
 * fetch data from TMDB database (doInBackground)
 * update the adapter accordingly (onPostExecute)
 * Created by Abhishek on 18-12-2015.
 */
public class FetchMoviesDataTask extends AsyncTask<String, Void, Boolean> {
    ///////////////////////////////////////////////////////////////////////////
    // Constants
    ///////////////////////////////////////////////////////////////////////////
    private static final String LOG_TAG = FetchMoviesDataTask.class.getSimpleName();
    private final DiscoverMoviesFragment parentFragment;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////
    public FetchMoviesDataTask(DiscoverMoviesFragment fragment) {
        parentFragment = fragment;
    }

    /**
     * Gets data from TMDB database,
     * parses using the parse method,
     * @param params 2 parameters:
     *               sortMethod: popular/top_rated
     *               page: page no to retrieve data from
     * @return An array of type MovieData containing details of movies.
     */
    @Override
    protected Boolean doInBackground(String... params) {
        Log.v(LOG_TAG, "doInBackground execution started!");
        Boolean success = false;

        String movieDataJSONStr;

        //URL Parameters
        final String API_KEY = parentFragment.getString(R.string.api_key);//replace with your TMDB API key
        final String PAGE_NO = params.length>1?params[1]:"1";//Getting page to implement infinite scrolling


        // Constructing the URL for the TMDB query
        //Required keys for Constructing the URL
        final String SORT_METHOD = params.length!=0?params[0]:"popular";//popular or top_rated
        final String TMDB_DISCOVER_BASE_URL = "http://api.themoviedb.org/3/movie/" +
                SORT_METHOD + "?";
        final String API_KEY_PARAM = "api_key";
        final String PAGE_NO_PARAM = "page";

        Uri tmdbDiscoverUri = Uri.parse(TMDB_DISCOVER_BASE_URL)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(PAGE_NO_PARAM, PAGE_NO)
                .build();

        URL tmdbDiscoverURL = null;
        try {
            tmdbDiscoverURL = new URL(tmdbDiscoverUri.toString());
            Log.i(LOG_TAG, "Constructed URL: " + tmdbDiscoverURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Getting JSON response from TMDB URL
        movieDataJSONStr = Util.FetchDataFromUrl(tmdbDiscoverURL);

        Log.i(LOG_TAG, "Response JSON : " + movieDataJSONStr);

        if(movieDataJSONStr != null) {
            //Got movieDataJSONStr, now parse meaningful values and insert into db
            try {
                Util.getMoviesDataFromJSON(movieDataJSONStr, parentFragment.getActivity());
                success = true;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing data from JSON : " + e);
            }
        }

        Log.v(LOG_TAG, "doInBackground finished executing");
        return success;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);

        if(!success){
            parentFragment.onDownloadFailed();
        }

    }
}
