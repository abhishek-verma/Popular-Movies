package com.abhishek.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

public class DiscoverMoviesFragment extends Fragment{
    private final String SORT_ORDER_POPULAR = "popular", SORT_ORDER_TOP_RATED = "top_rated";

    private GridView gridView;
    public MovieAdapter movieAdapter;
    private String sortOrder = SORT_ORDER_POPULAR;
//    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover_movies, container);
        gridView = (GridView) rootView.findViewById(R.id.movieGridView);
        movieAdapter = new MovieAdapter(getActivity(), R.layout.grid_item, new ArrayList<MovieData>());
        gridView.setAdapter(movieAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateAdapterData();
    }

    /**
     * Adds initial data to the adapter
     * according to the sorting order
     * Executes FetchMoviesDataTask
     * @return
     */
    public void updateAdapterData() {
        movieAdapter.clear();
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
                    updateAdapterData();
                }
                break;
            case R.id.menu_item_sort_top_rated:
                if(!item.isChecked()){
                    item.setChecked(true);
                    sortOrder = SORT_ORDER_TOP_RATED;
                    updateAdapterData();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}