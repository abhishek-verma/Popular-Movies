package com.abhishek.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    final String LOG_TAG = DetailsActivity.class.getSimpleName();
    private ImageView backdropImgV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        backdropImgV = (ImageView) findViewById(R.id.movieBackdropImg);

//        NestedScrollView detailsScrlV = (NestedScrollView) findViewById(R.id.detailsScrlV);
        /*
         TODO
         This was supposed to work,
         although, not enough time,
         I'll be back on this,
         but,
         this was supposed to work.

         FIXME
         Listener, apparently, has gone deaf
         and isn't listening to whatever it was supposed to listen.

         On top of that, AS says,
         "Requires API level 23"
         But this should work fine after release of Support library 23.1.*
         */
//        detailsScrlV.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                backdropImgV.setAlpha(getbackdropImgAlpha(scrollY));
//            }
//        });

        setBackdropImgV();
    }

    private void setBackdropImgV(){
        Intent launchIntent = getIntent();

        if(launchIntent!=null && launchIntent.hasExtra(MovieData.EXTRA_MOVIE_DATA)){
            MovieData movieData = launchIntent.getParcelableExtra(MovieData.EXTRA_MOVIE_DATA);
            Picasso.with(this).load(movieData.getBackdropImgPath()).into(backdropImgV);
        }
    }

    //Untested
//    public float getbackdropImgAlpha(int scrlDist){
//        int maxScrl = getResources().getDimensionPixelSize(R.dimen.app_bar_height);
//        float alpha = scrlDist/maxScrl;
//        if(alpha < 0)   alpha=0;
//        Log.i(LOG_TAG, "getbackdropImgAlpha result: " + alpha);
//        return alpha;
//    }
}
