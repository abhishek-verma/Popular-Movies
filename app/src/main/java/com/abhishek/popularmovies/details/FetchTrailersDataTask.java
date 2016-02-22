package com.abhishek.popularmovies.details;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.abhishek.popularmovies.R;
import com.abhishek.popularmovies.Util;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Abhishek on 2/19/2016.
 */
public class FetchTrailersDataTask extends AsyncTask<String, Void, DataObjects.TrailerData[]> {
    ///////////////////////////////////////////////////////////////////////////
    // Constants
    ///////////////////////////////////////////////////////////////////////////
    private final String LOG_TAG = FetchTrailersDataTask.class.getSimpleName();

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////
    private DetailsFragment parentFragment;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////
    public FetchTrailersDataTask(DetailsFragment fragment){
        parentFragment = fragment;
    }

    @Override
    protected DataObjects.TrailerData[] doInBackground(String... params) {
        Log.v(LOG_TAG, "doInBackground execution started!");

        DataObjects.TrailerData[] trailerData = null;
        String trailerDataJsonStr;

        if(params.length < 1) return null;

        final String API_KEY  = parentFragment.getString(R.string.api_key);


        final String MOVIE_ID_PATH = params[0];
        final String TRAILER_PATH = "trailers";
        final String BASE_URL = "http://api.themoviedb.org/3/movie/" + MOVIE_ID_PATH + "/" + TRAILER_PATH + "?";

        final String API_KEY_PARAM = "api_key";

        Uri tmdbMovieTrailerUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL tmdbMovieTrailerUrl = null;
        try {
            tmdbMovieTrailerUrl = new URL(tmdbMovieTrailerUri.toString());
            Log.i(LOG_TAG, "Constructed URL: " + tmdbMovieTrailerUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        trailerDataJsonStr = Util.FetchDataFromUrl(tmdbMovieTrailerUrl);

        Log.i(LOG_TAG, "doInBackground: Response Json: " + trailerDataJsonStr);



        if(trailerDataJsonStr != null){
            try {
                trailerData = Util.parseTrailerDataFromJson(trailerDataJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing data from JSON : " + e);
            }
        }

        Log.v(LOG_TAG, "doInBackground finished executing");
        return trailerData;
    }

    @Override
    protected void onPostExecute(DataObjects.TrailerData[] trailerDatas) {
        super.onPostExecute(trailerDatas);

        if(trailerDatas == null || trailerDatas.length == 0){
            parentFragment.onDownloadFailed();
            parentFragment.movieTrailerRecV.setVisibility(View.GONE);
            return;
        }


        //For debugging: get the trailer data and append each data to tmpTrailerTxtV
//        TextView trailerTxtV = (TextView) parentFragment.getView().findViewById(R.id.tmpTrTxtV);
//        String tmpResData = (String) trailerTxtV.getText();
//        for (DataObjects.TrailerData data : trailerDatas) {
//            tmpResData = tmpResData + "\n" + data.trailerName + "\n" + data.sourceUrl + "\n" + data.youtubeThumg;
//        }
//        trailerTxtV.setText(tmpResData);

        parentFragment.movieTrailerRecV.setVisibility(View.VISIBLE);
        List<DataObjects.TrailerData> trailerDataList = Arrays.asList(trailerDatas);
//        parentFragment.mTrailerDataList.clear();
        parentFragment.mTrailerDataList.addAll(trailerDataList);
        parentFragment.trailerAdapter.notifyDataSetChanged();

        //For sharing youtube url
        parentFragment.youtubeShareUrl = trailerDatas[0].sourceUrl;

        if(parentFragment.mShareActionProvider != null)
            parentFragment.mShareActionProvider.setShareIntent(parentFragment.createShareForecastIntent());
    }
}
