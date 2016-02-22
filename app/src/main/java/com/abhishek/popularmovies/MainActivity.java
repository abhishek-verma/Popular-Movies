package com.abhishek.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.abhishek.popularmovies.data.MovieContract;
import com.abhishek.popularmovies.details.DetailsActivity;
import com.abhishek.popularmovies.details.DetailsFragment;

public class MainActivity extends AppCompatActivity implements DiscoverMoviesFragment.Callback{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean mTwoPane;
    private static final String DETAIL_FRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailsFragment())
                        .commit();
            }
        }
        else {
            mTwoPane = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//
//        DiscoverMoviesFragment dmf = (DiscoverMoviesFragment)
//                getSupportFragmentManager().findFragmentById(R.id.fragment_movies);
    }

    @Override
    public void onItemClicked(Uri uri) {
        Log.i(LOG_TAG, "onItemClicked: started");
        if(mTwoPane){
            Log.i(LOG_TAG, "onItemClicked: dual pane : replacing fragment with one with uri: " + uri.toString());

            DetailsFragment df = DetailsFragment.newInstance(uri.toString());
            getSupportFragmentManager().beginTransaction()
            .replace(R.id.movie_detail_container, df)
            .commit();
        }
        else{
            Log.i(LOG_TAG, "onItemClicked: not dual pane : call activity with uri: " + uri.toString());

            Intent detailsIntent = new Intent(this, DetailsActivity.class);
            detailsIntent.setData(uri);
            startActivity(detailsIntent);
        }
    }

    @Override
    public void onLoadComplete(Uri uri) {
        if(mTwoPane){
            Log.i(LOG_TAG, "onLoadComplete: dual pane : replacing fragment with one with uri: " + uri.toString());

            DetailsFragment df = DetailsFragment.newInstance(uri.toString());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, df)
                    .commit();
        }
    }
}
