package com.abhishek.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * This fragment will contain details about a particular
 * movie, including the information shown in the DiscoverMoviesFragment
 * and a whole bunch of other information.
 *
 * I still fail to fill in enough information about a movie to cover a single screen.
 *
 * TODO Fill at least a page with some movie information in project 2
 */
public class DetailsFragment extends Fragment {
    public DetailsFragment() {
    }

    /**
     * The methods gets the launch intent,
     * retrieves to movieData,
     * gets references to different views form the fragment/activity,
     * inflates data into them from the retrieved movieData
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ImageView movieThumbImgV = (ImageView) rootView.findViewById(R.id.movieThumbImgV);
        TextView movieNameTxtV = (TextView) rootView.findViewById(R.id.movieNameTxtV);
        TextView movieRatingTxtV = (TextView) rootView.findViewById(R.id.movieRatingTxtV);
        TextView movieOverviewTxtV = (TextView) rootView.findViewById(R.id.movieOverviewTxtV);
        TextView movieReleaseDateTxtV = (TextView) rootView.findViewById(R.id.movieReleaseDateTxtV);

        CardView overviewCrdV = (CardView) rootView.findViewById(R.id.overviewCrdV);
        overviewCrdV.setCardBackgroundColor(R.color.cardview_dark_background);

        Intent launchIntent = getActivity().getIntent();

        if(launchIntent!=null && launchIntent.hasExtra(MovieData.EXTRA_MOVIE_DATA)){
            MovieData movieData = launchIntent.getParcelableExtra(MovieData.EXTRA_MOVIE_DATA);
            Picasso.with(getContext()).load(movieData.getThumbImgPath()).into(movieThumbImgV);
            movieNameTxtV.setText(movieData.getMovieName());
            movieRatingTxtV.setText(String.format("%s \u2605", movieData.getRating()));
            movieOverviewTxtV.setText(movieData.getOverview());
            movieReleaseDateTxtV.setText(String.format("Release Date: %s", movieData.getReleaseDate()));
        }
        return rootView;
    }
}
