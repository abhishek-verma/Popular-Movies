package com.abhishek.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Adapter attached to mGridView in discover movies fragment
 * Contains views from data items of type movieData
 * Created by Abhishek on 17-12-2015.
 */
public class MovieAdapter extends CursorAdapter {
    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////
    private final Context context;

    ///////////////////////////////////////////////////////////////////////////
    // Constructor(s)
    ///////////////////////////////////////////////////////////////////////////
    public MovieAdapter(Context context,  Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Overridden Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        MovieItemHolder movieItemHolder;

        View movieItemView = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);

        movieItemHolder = new MovieItemHolder(movieItemView);
        movieItemView.setTag(movieItemHolder);

        return movieItemView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MovieItemHolder movieItemHolder = (MovieItemHolder) view.getTag();

        String title = cursor.getString(DiscoverMoviesFragment.COL_INDEX_MOVIE_TITLE);
        String posterPath = cursor.getString(DiscoverMoviesFragment.COL_INDEX_POSTER_PATH);
        String rating = context.getString(R.string.format_rating, cursor.getDouble(DiscoverMoviesFragment.COL_INDEX_VOTE_AVERAGE));

        Picasso.with(context).load(posterPath).into(movieItemHolder.movieThumbImgV);
        movieItemHolder.movieNameTxtV.setText(title);
        movieItemHolder.movieRatingTxtV.setText(rating);


    }

    ///////////////////////////////////////////////////////////////////////////
    // Custom Methods
    ///////////////////////////////////////////////////////////////////////////
    /**
     * An holder is used,
     * coz if not used, the findViewById method will be called every time a new item is shown in the grid view,
     * and that's just frustrating, even for a machine,
     * and which of course also takes a lot of extra time,
     * while the holder sort of saves the location of the views inside the parent view
     * which save the system from searching for a view again and again.
     */
    public class MovieItemHolder {
        public ImageView movieThumbImgV;
        public TextView movieNameTxtV;
        public TextView movieRatingTxtV;

        public MovieItemHolder(View view){
            this.movieThumbImgV = (ImageView) view.findViewById(R.id.movieThumbImgV);
            this.movieNameTxtV = (TextView) view.findViewById(R.id.movieNameTxtV);
            this.movieRatingTxtV = (TextView) view.findViewById(R.id.movieRatingTxtV);
        }
    }
}
