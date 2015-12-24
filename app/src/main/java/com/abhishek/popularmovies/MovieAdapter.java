package com.abhishek.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Abhishek on 17-12-2015.
 */
public class MovieAdapter extends ArrayAdapter<MovieData> {
    Context context;
    ArrayList<MovieData> moviesData;

    public MovieAdapter(Context context, int resource, ArrayList<MovieData> data) {
        super(context, resource, data);
        this.context = context;
        moviesData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View movieItemView = convertView;
        MovieItemHolder movieItemHolder;

        if (movieItemView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            movieItemView = inflater.inflate(R.layout.grid_item, parent, false);

            movieItemHolder = new MovieItemHolder();
            movieItemHolder.movieThumbImgV = (ImageView) movieItemView.findViewById(R.id.movieThumbImgV);
            movieItemHolder.movieNameTxtV = (TextView) movieItemView.findViewById(R.id.movieNameTxtV);
            movieItemHolder.movieRatingTxtV = (TextView) movieItemView.findViewById(R.id.movieRatingTxtV);
            movieItemView.setTag(movieItemHolder);
        } else {
            movieItemHolder = (MovieItemHolder) movieItemView.getTag();
        }

        MovieData movieData = moviesData.get(position);
        Picasso.with(context).load(movieData.getThumbImgPath()).into(movieItemHolder.movieThumbImgV);
        movieItemHolder.movieNameTxtV.setText(movieData.getMovieName());
        movieItemHolder.movieRatingTxtV.setText(String.valueOf(movieData.getRating()) + " \u2605");


        return movieItemView;
    }

    /**
     * An holder is used,
     * coz if not used, the findViewById method will be called every time a new item is shown in the grid view,
     * and that's just frustrating, even for a machine,
     * and which ofcourse also takes a lot of extra time,
     * while the holder sort of saves the location of the views inside the parent view
     * which save the system from searching for a view again and again.
     */
    public class MovieItemHolder {
        public ImageView movieThumbImgV;
        public TextView movieNameTxtV;
        public TextView movieRatingTxtV;
    }
}
