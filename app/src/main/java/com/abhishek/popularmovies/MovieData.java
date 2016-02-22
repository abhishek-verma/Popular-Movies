package com.abhishek.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Contains data relevant to a movie item in the grid view in the DiscoverMoviesFragment
 *
 * If you ever add a field, in addition do the following
 *  -add it in both the constructors,
 *  -add a getter method
 *  -add in writeToParcel
 *  -that's it, you're done!
 * Created by Abhishek on 17-12-2015.
 */
public class MovieData implements Parcelable{
    ///////////////////////////////////////////////////////////////////////////
    // Constants
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////
    private final String thumbImgPath;
    private final String movieName;
    private final Double rating;
    private final String overview;
    private final String backdropImgPath;
    private final String releaseDate;
    private final long movieId;
    private final double popularity;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////
    public MovieData(String thumbImgPath, String movieName, Double rating, String overview, String backdropImgPath, String releaseDate, long movieId, double popularity) {
        this.thumbImgPath = thumbImgPath;
        this.movieName = movieName;
        this.rating = rating;
        this.overview = overview;
        this.backdropImgPath = backdropImgPath;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
        this.popularity = popularity;
    }

    private MovieData(Parcel in){
        thumbImgPath = in.readString();
        movieName = in.readString();
        rating = in.readDouble();
        overview = in.readString();
        backdropImgPath = in.readString();
        releaseDate = in.readString();
        movieId = in.readLong();
        popularity = in.readDouble();
    }


    ///////////////////////////////////////////////////////////////////////////
    // Custom Methods
    ///////////////////////////////////////////////////////////////////////////
    public String getThumbImgPath() {
        return thumbImgPath;
    }

    public String getMovieName() {
        return movieName;
    }

    public Double getRating() {
        return rating;
    }

    public String getOverview(){
        return overview;
    }

    public String getBackdropImgPath() {
        return backdropImgPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public long getMovieId(){
        return movieId;
    }

    public double getPopularity(){
        return popularity;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Overridden Methods
    ///////////////////////////////////////////////////////////////////////////
    /**
     * I still wonder what this method does,
     * all I know is that
     * most of the time it just sits there
     * returning an egg.
     *
     * There's a rumour that sometimes
     * it does something with CONTENTS_FILE_DESCRIPTOR
     * before returning an egg or, I don't know, a chicken,
     * for some 'special' classes.
     * Discrimination people!
     *
     * Update:
     * http://stackoverflow.com/questions/4076946/parcelable-where-when-is-describecontents-used/4914799#4914799
     * The above linked page has a great explanation.
     *
     * describeContents returns the constant CONTENTS_FILE_DESCRIPTOR
     * if the class has a field of type FileDescriptor which needs to be parsed in
     * and in all other cases it returns, well, an egg.
     *
     * @return egg sometimes chicken
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumbImgPath);
        dest.writeString(movieName);
        dest.writeDouble(rating);
        dest.writeString(overview);
        dest.writeString(backdropImgPath);
        dest.writeString(releaseDate);
        dest.writeLong(movieId);
        dest.writeDouble(popularity);
    }

    /**
     * It seems like a C++ programmer designed Parcelable
     * and at some point he realized: Oh, damn, there is no
     * multiple inheritance in Java
     */
    public static final Parcelable.Creator<MovieData> CREATOR
            = new Parcelable.Creator<MovieData>() {

        @Override
        public MovieData createFromParcel(Parcel source) {
            return new MovieData(source);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
}
