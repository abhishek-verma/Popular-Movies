package com.abhishek.popularmovies;

/**
 * A class can implement OnDownloadFailedListener
 * if it uses some method to access some online data.
 *
 * It needs to override onDownloadFailed method to handle situations
 * when download fails.
 *
 * Created by Abhishek on 25-12-2015.
 */
public interface OnDownloadFailedListener {
    void onDownloadFailed();
}
