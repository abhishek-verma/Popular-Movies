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
class FetchMoviesDataTask extends AsyncTask<String, Void, MovieData[]> {
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
    protected MovieData[] doInBackground(String... params) {

        Log.v(LOG_TAG, "doInBackground execution started!");

        HttpURLConnection tmdbURLConnection = null;
        BufferedReader reader = null;

        String movieDataJSONStr = null;

        //URL Parameters
        final String API_KEY = parentFragment.getString(R.string.api_key);//replace with your TMDB API key
        final String PAGE_NO = params.length>12?params[1]:"1";//Getting page to implement infinite scrolling


        try {
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

            URL tmdbDiscoverURL = new URL(tmdbDiscoverUri.toString());
            Log.i(LOG_TAG, "Constructed URL: " + tmdbDiscoverURL);

            //Create a request to TMDB, and open the connection
            tmdbURLConnection = (HttpURLConnection) tmdbDiscoverURL.openConnection();
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

            movieDataJSONStr = responseStrBuf.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
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

        Log.i(LOG_TAG, "Response JSON : " + movieDataJSONStr);

        //Got movieDataJSONStr, now parsing into meaningful MovieData objects
        MovieData[] moviesData = null;
        try{
            moviesData = Util.getMoviesDataFromJSON(movieDataJSONStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing data from JSON : " + e);
        }

        Log.v(LOG_TAG, "doInBackground finished executing");
        return moviesData;
    }

    @Override
    protected void onPostExecute(MovieData[] moviesData) {
        super.onPostExecute(moviesData);

        if(moviesData !=null){
            parentFragment.movieAdapter.addAll(moviesData);
        }
        else{
            parentFragment.onDownloadFailed();
        }
    }
}
