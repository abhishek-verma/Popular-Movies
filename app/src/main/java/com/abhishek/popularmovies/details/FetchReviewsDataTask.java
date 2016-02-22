package com.abhishek.popularmovies.details;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Abhishek on 2/19/2016.
 */
public class FetchReviewsDataTask extends AsyncTask<String, Void, DataObjects.ReviewData[]> {
    ///////////////////////////////////////////////////////////////////////////
    // Constants
    ///////////////////////////////////////////////////////////////////////////
    private final String LOG_TAG = FetchReviewsDataTask.class.getSimpleName();

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////
    private DetailsFragment parentFragment;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////
    public FetchReviewsDataTask(DetailsFragment fragment){
        parentFragment = fragment;
    }

    @Override
    protected DataObjects.ReviewData[] doInBackground(String... params) {
        Log.v(LOG_TAG, "doInBackground execution started!");

        DataObjects.ReviewData[] reviewData = null;
        String reviewDataJsonStr;

        if(params.length < 1) return null;

        final String API_KEY  = parentFragment.getString(R.string.api_key);


        final String MOVIE_ID_PATH = params[0];
        final String REVIEW_PATH = "reviews";
        final String BASE_URL = "http://api.themoviedb.org/3/movie/" + MOVIE_ID_PATH + "/" + REVIEW_PATH + "?";

        final String API_KEY_PARAM = "api_key";

        Uri tmdbMovieReviewUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL tmdbMovieReviewURL = null;
        try {
            tmdbMovieReviewURL = new URL(tmdbMovieReviewUri.toString());
            Log.i(LOG_TAG, "Constructed URL: " + tmdbMovieReviewUri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        reviewDataJsonStr = Util.FetchDataFromUrl(tmdbMovieReviewURL);

        Log.i(LOG_TAG, "doInBackground: Response Json: " + reviewDataJsonStr);



        if(reviewDataJsonStr != null){
            try {
                reviewData = Util.parseReviewDataFromJson(reviewDataJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing data from JSON : " + e);
            }
        }

        Log.v(LOG_TAG, "doInBackground finished executing");
        return reviewData;
    }

    @Override
    protected void onPostExecute(DataObjects.ReviewData[] reviewDatas) {
        super.onPostExecute(reviewDatas);

        if(reviewDatas == null || reviewDatas.length == 0){
            parentFragment.onDownloadFailed();
            parentFragment.movieReviewRecV.setVisibility(View.GONE);
            return;
        }

        //get the review data and append each data to tmpReviewTxtV
//        TextView reviewTxtV = (TextView) parentFragment.getView().findViewById(R.id.tmpRvTxtV);
//        String tmpResData = reviewTxtV.getText() + "\n";
//        for (DataObjects.ReviewData data : reviewDatas) {
//            tmpResData = "\n" + tmpResData + "\n" + data.author + "\n" + data.content + "\n";
//        }
//        reviewTxtV.setText(tmpResData);


        parentFragment.movieReviewRecV.setVisibility(View.VISIBLE);
        List<DataObjects.ReviewData> reviewDataList = Arrays.asList(reviewDatas);
//        parentFragment.mReviewDataList.clear();
        parentFragment.mReviewDataList.addAll(reviewDataList);
        parentFragment.reviewAdapter.notifyDataSetChanged();
    }
}
