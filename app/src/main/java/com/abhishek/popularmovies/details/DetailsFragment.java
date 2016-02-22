package com.abhishek.popularmovies.details;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.popularmovies.OnDownloadFailedListener;
import com.abhishek.popularmovies.R;
import com.abhishek.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This fragment will contain details about a particular
 * movie, including the information shown in the DiscoverMoviesFragment
 * and a whole bunch of other information.
 * <p/>
 * I still fail to fill in enough information about a movie to cover a single screen.
 * <p/>
 * TODO Fill at least a page with some movie information in project 2
 */
public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnDownloadFailedListener {
    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();

    public static final int MOVIE_DETAILS_LOADER = 1;

    static final int COL_INDEX_MOVIE_ID = 0;
    static final int COL_INDEX_MOVIE_TITLE = 1;
    static final int COL_INDEX_RELEASE_DATE = 2;
    static final int COL_INDEX_VOTE_AVERAGE = 3;
    static final int COL_INDEX_POSTER_PATH = 4;
    static final int COL_INDEX_BACKDROP_PATH = 5;
    static final int COL_INDEX_OVERVIEW = 6;
    static final int COL_INDEX_POPULARITY = 7;
    private static final String[] MOVIE_DETAILS_CURSOR_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVEREAGE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_POPULARITY
    };;

    private ImageView movieThumbImgV;
    private TextView movieNameTxtV;
    private TextView movieRatingTxtV;
    private TextView movieOverviewTxtV;
    private TextView movieReleaseDateTxtV;

    public RecyclerView movieTrailerRecV, movieReviewRecV;
    TrailerAdapter trailerAdapter;
    public ArrayList<DataObjects.TrailerData> mTrailerDataList = new ArrayList<>();
    ReviewAdapter reviewAdapter;
    public ArrayList<DataObjects.ReviewData> mReviewDataList = new ArrayList<>();

    public Uri mUri;

    // For sharing
    public String movieName, youtubeShareUrl;
    public ShareActionProvider mShareActionProvider;

    public DetailsFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * Create a new instance of DetailFragment, initialized to
     * show the text at 'index'.
     * @param uriStr
     */
    public static DetailsFragment newInstance(String uriStr){
        DetailsFragment f = new DetailsFragment();

        //Supply index input as an argument
        Bundle args = new Bundle();
        args.putString("uri", uriStr.toString());

        f.setArguments(args);
        return f;
    }

    public String getPassedUri(){
        if(getArguments() != null)
            return getArguments().getString("uri", null);
        else
            return null;
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

        CardView overviewCrdV = (CardView) rootView.findViewById(R.id.overviewCrdV);
        overviewCrdV.setCardBackgroundColor(R.color.cardview_dark_background);


        movieThumbImgV = (ImageView) rootView.findViewById(R.id.movieThumbImgV);
        movieNameTxtV = (TextView) rootView.findViewById(R.id.movieNameTxtV);
        movieRatingTxtV = (TextView) rootView.findViewById(R.id.movieRatingTxtV);
        movieOverviewTxtV = (TextView) rootView.findViewById(R.id.movieOverviewTxtV);
        movieReleaseDateTxtV = (TextView) rootView.findViewById(R.id.movieReleaseDateTxtV);


        movieTrailerRecV = (RecyclerView) rootView.findViewById(R.id.trailerRecV);
        trailerAdapter = new TrailerAdapter(mTrailerDataList, getActivity());
        movieTrailerRecV.setAdapter(trailerAdapter);
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(getActivity());
        trailerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        movieTrailerRecV.setLayoutManager(trailerLayoutManager);
        movieTrailerRecV.hasFixedSize();

        movieReviewRecV = (RecyclerView) rootView.findViewById(R.id.reviewRecV);
        reviewAdapter = new ReviewAdapter(mReviewDataList, getActivity());
        movieReviewRecV.setAdapter(reviewAdapter);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getActivity());
        reviewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        movieReviewRecV.setLayoutManager(reviewLayoutManager);

        String uriStr = getPassedUri();
        if(uriStr != null) {
            mUri = Uri.parse(getPassedUri());

            getLoaderManager().initLoader(MOVIE_DETAILS_LOADER,
                    null,
                    this);

            updateMovieDetails();
        }

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details_fragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (youtubeShareUrl != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_set_fav:
                addRemoveMovieFromFav(MovieContract.MovieEntry.getMovieIdFromUri(mUri));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        if(movieName!=null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    String.format("Check out the %s trailer here, %s", movieName, youtubeShareUrl));
        }
        else{
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    String.format("Check out the youtube trailer, %s", youtubeShareUrl));
        }
        return shareIntent;
    }

    /**
     * Inserts the provided movie id to fav db table, if it isn't already in there,
     * removes otherwise.
     *
     * Better implementation, insert the given id to db, if it fails, remove it.
     *
     * @param id the id of the movie to be inserted/remmoved
     * @return success state of the operation
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean addRemoveMovieFromFav(String id){
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor c = contentResolver.query(MovieContract.FavEntry.CONTENT_URI,
                null,
                MovieContract.FavEntry.TABLE_NAME + "." + MovieContract.FavEntry._ID + "=?",
                new String[]{id},
                null, null);

        if(c.moveToFirst()){
            Log.i(LOG_TAG, "addRemoveMovieFromFav: movie already in fav, removing movie, id:" + c.getString(0));
            int rowsDeleted = contentResolver.delete(MovieContract.FavEntry.CONTENT_URI,
                    MovieContract.FavEntry._ID + "=?",
                    new String[]{id});

            if(rowsDeleted>0){
                Log.i(LOG_TAG, "addRemoveMovieFromFav: movie with id " + id + " removed from fav");
                Toast.makeText(getActivity(), "Movie removed from favourites", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }


        ContentValues favMovIdContentValues = new ContentValues();
        favMovIdContentValues.put(MovieContract.FavEntry._ID, id);
        Uri inserted = contentResolver.insert(MovieContract.FavEntry.CONTENT_URI,
                favMovIdContentValues);

        if(inserted!=null) {
            Log.d(LOG_TAG, "onOptionsItemSelected: Movie added to fav table, URI: " + inserted);
            Toast.makeText(getActivity(), "Movie added to favourites", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(mUri == null)
            return null;

        return new CursorLoader(getActivity(),
                mUri,
                MOVIE_DETAILS_CURSOR_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (!data.moveToFirst())
            return;

        String thumbPath = data.getString(COL_INDEX_POSTER_PATH);
        String title = data.getString(COL_INDEX_MOVIE_TITLE);
        Double rating = data.getDouble(COL_INDEX_VOTE_AVERAGE);
        String overview = data.getString(COL_INDEX_OVERVIEW);
        String releaseDate = data.getString(COL_INDEX_RELEASE_DATE);
        String backdropPath = data.getString(COL_INDEX_BACKDROP_PATH);

        //An imageView from main activity
        ImageView backdropImgV = (ImageView) getActivity().findViewById(R.id.movieBackdropImg);

        Picasso.with(getContext()).load(thumbPath).into(movieThumbImgV);
        movieNameTxtV.setText(title);
        movieRatingTxtV.setText(getString(R.string.format_rating, rating));
        movieOverviewTxtV.setText(overview);
        movieReleaseDateTxtV.setText(String.format("Release Date: %s", releaseDate));
        if(backdropImgV!= null)
            Picasso.with(getContext()).load(backdropPath).into(backdropImgV);

        //saving movie name in field for sharing
        movieName = title;

        if(mShareActionProvider != null)
           mShareActionProvider.setShareIntent(createShareForecastIntent());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDownloadFailed() {
        Snackbar
                .make(getView(), R.string.snackbar_download_failed_msg, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateMovieDetails();
                    }
                })
                .show();
    }

    private void updateMovieDetails() {
        if(mUri == null)
            return;

        FetchTrailersDataTask fetchTrailersDataTask = new FetchTrailersDataTask(this);
        fetchTrailersDataTask.execute(MovieContract.MovieEntry.getMovieIdFromUri(mUri));

        FetchReviewsDataTask fetchReviewsDataTask = new FetchReviewsDataTask(this);
        fetchReviewsDataTask.execute(MovieContract.MovieEntry.getMovieIdFromUri(mUri));
    }
}
