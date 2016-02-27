package com.abhishek.popularmovies.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.abhishek.popularmovies.R;

public class DetailsActivity extends AppCompatActivity {
    final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        if(savedInstanceState == null) {
            DetailsFragment df = DetailsFragment.newInstance(getIntent().getData().toString());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, df)
                    .commit();
        }
    }
}
