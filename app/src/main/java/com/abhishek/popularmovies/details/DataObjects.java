package com.abhishek.popularmovies.details;

/**
 * Created by Abhishek on 2/15/2016.
 */
public class DataObjects {
    public static class ReviewData{
        // Writer of the review
        String author;
        // String containing actual review
        String content;

        public ReviewData(String author, String content){
            this.author = author;
            this.content = content;
        }
    }
    public static class TrailerData{
        // Name of the trailer
        String trailerName;

        // Source of the trailer
        String sourceUrl;//Append into youtube watch link like : https://www.youtube.com/watch?v=<trailer_src>

        //Source for youtube thumbnail
        String youtubeThumg;//http://img.youtube.com/vi/<trailer_src>/0.jpg

        public TrailerData(String name, String src){
            trailerName = name;
            sourceUrl = "https://www.youtube.com/watch?v=" + src;
            youtubeThumg = "http://img.youtube.com/vi/" + src + "/0.jpg";
        }
    }
}
