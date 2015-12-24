package com.abhishek.popularmovies;

import android.widget.ImageView;

import java.net.URL;

/**
 * Created by Abhishek on 17-12-2015.
 */
public class MovieData {
    private String thumbImgPath;
    private String movieName;
    private Double rating;

    public MovieData(String thumbImgPath, String movieName, Double rating) {
        this.thumbImgPath = thumbImgPath;
        this.movieName = movieName;
        this.rating = rating;
    }

    public String getThumbImgPath() {
        return thumbImgPath;
    }

    public void setThumbImgPath(String thumbImgPath) {
        this.thumbImgPath = thumbImgPath;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}
