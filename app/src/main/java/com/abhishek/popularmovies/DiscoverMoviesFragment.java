package com.abhishek.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.widget.ListView;

import com.abhishek.popularmovies.data.MovieContract;
import com.abhishek.popularmovies.data.MovieProvider;

public class DiscoverMoviesFragment extends Fragment implements OnDownloadFailedListener, LoaderManager.LoaderCallbacks<Cursor>{
    public static final String SORT_ORDER_POPULAR = "popular", SORT_ORDER_TOP_RATED = "top_rated";

    private static final String SELECTED_KEY = "selectedPos";
    private int mPosition = -1;

    Boolean displayFav = false;

    public MovieAdapter movieAdapter;
    private String sortOrder = SORT_ORDER_POPULAR;

    public static final int MOVIE_DATA_LOADER = 0;

    private static String[] MOVIE_DATA_CURSOR_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVEREAGE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_POPULARITY
    } ;

    static final int COL_INDEX_MOVIE_ID = 0;
    static final int COL_INDEX_MOVIE_TITLE = 1;
    static final int COL_INDEX_RELEASE_DATE = 2;
    static final int COL_INDEX_VOTE_AVERAGE = 3;
    static final int COL_INDEX_POSTER_PATH = 4;
    static final int COL_INDEX_BACKDROP_PATH = 5;
    static final int COL_INDEX_OVERVIEW = 6;
    static final int COL_INDEX_POPULARITY = 7;

    Callback mCallbackListener;
    GridView mGridView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Callback) {
            mCallbackListener = (Callback) context;
        } else {
            throw new ClassCastException(
                    context.toString()
                            + " must implement ItemsListFragment.OnListItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover_movies, container);

        mGridView = (GridView) rootView.findViewById(R.id.movieGridView);

        movieAdapter = new MovieAdapter(getActivity(), null, 0);

        mGridView.setAdapter(movieAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieAdapter adapter = (MovieAdapter) parent.getAdapter();
                Cursor c = adapter.getCursor();
                if (c != null && c.moveToPosition(position)) {
                    mCallbackListener.onItemClicked(MovieContract.MovieEntry.buildMovieWithIdUri(c.getLong(COL_INDEX_MOVIE_ID)));
                }
                mPosition = position;
            }
        });

        getLoaderManager().initLoader(MOVIE_DATA_LOADER,
                null,
                this);

        if(savedInstanceState!=null
                && savedInstanceState.containsKey(SELECTED_KEY)){
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(mPosition != ListView.INVALID_POSITION){
            outState.putInt(SELECTED_KEY, mPosition);
        }

        super.onSaveInstanceState(outState);
    }

    /**
     * Adds initial data to the adapter
     * according to the sorting order
     * Executes FetchMoviesDataTask
     * @return nada
     */
    private void updateMovieData() {
        FetchMoviesDataTask fetchMoviesDataTask = new FetchMoviesDataTask(this);
        fetchMoviesDataTask.execute(sortOrder);//get short method param using DefaultSharedPrefs
    }

    //To implement infinite scrolling in Project 2
//    public void appendDataToAdapter(){
//        FetchMoviesDataTask fetchMoviesDataTask = new FetchMoviesDataTask(this);
//        fetchMoviesDataTask.execute(sortOrder, String.valueOf(page));
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.menu_discover_movies_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_sort_popular:
                if(!item.isChecked()){
                    item.setChecked(true);
                    sortOrder = SORT_ORDER_POPULAR;
                    updateMovieData();
                }
                break;
            case R.id.menu_item_sort_top_rated:
                if(!item.isChecked()){
                    item.setChecked(true);
                    sortOrder = SORT_ORDER_TOP_RATED;
                    updateMovieData();
                }
                break;
            case R.id.menu_item_display_fav:
                    displayFav = !displayFav;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        getLoaderManager().restartLoader(MOVIE_DATA_LOADER,
                null,
                this);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDownloadFailed() {
        Snackbar
                .make(getView(), R.string.snackbar_download_failed_msg, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateMovieData();
                    }
                })
                .show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrderQueryStr = null;
        switch (sortOrder){
            case SORT_ORDER_POPULAR:
                sortOrderQueryStr = MovieProvider.pPopularMoviesSortOrder;
                break;
            case SORT_ORDER_TOP_RATED:
                sortOrderQueryStr = MovieProvider.pTopRatedMoviesSortOrder;
        }

        if(displayFav)
            return new CursorLoader(getActivity(),
                    MovieContract.FavEntry.CONTENT_URI,
                    MOVIE_DATA_CURSOR_COLUMNS,
                    null, null, sortOrderQueryStr);

        else
            return new CursorLoader(getActivity(),
                    MovieContract.MovieEntry.CONTENT_URI,
                    MOVIE_DATA_CURSOR_COLUMNS,
                    null, null, sortOrderQueryStr);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieAdapter.swapCursor(data);

        //if previous position exists select it
        if(mPosition != ListView.INVALID_POSITION) {
            mGridView.setSelection(mPosition);
        }
        //otherwise select the first position
        else {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    mCallbackListener.onLoadComplete(Uri.parse(msg.getData().getString("uri")));
                }
            };
            if (data.moveToFirst()) {
                Message uriMsg = new Message();
                Bundle b = new Bundle();
                b.putString("uri", MovieContract.MovieEntry.buildMovieWithIdUri(data.getLong(COL_INDEX_MOVIE_ID)).toString());
                uriMsg.setData(b);

                handler.sendMessage(uriMsg);
            }
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }

    public interface Callback {
        void onItemClicked(Uri uri);
        void onLoadComplete(Uri uri);
    }
}