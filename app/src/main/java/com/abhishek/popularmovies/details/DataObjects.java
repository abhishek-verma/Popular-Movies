package com.abhishek.popularmovies.details;

/**
 * Created by Abhishek on 2/15/2016.
 */
public class DataObjects {
    public static class ReviewData{
        // Writer of the review
        final String author;
        // String containing actual review
        final String content;

        public ReviewData(String author, String content){
            this.author = author;
            this.content = content;
        }
    }
    public static class TrailerData{
        // Name of the trailer
        final String trailerName;

        // Source of the trailer
        final String sourceUrl;//Append into youtube watch link like : https://www.youtube.com/watch?v=<trailer_src>

        //Source for youtube thumbnail
        final String youtubeThumb;//http://img.youtube.com/vi/<trailer_src>/0.jpg

        public TrailerData(String name, String src){
            trailerName = name;
            sourceUrl = "https://www.youtube.com/watch?v=" + src;
            youtubeThumb = "http://img.youtube.com/vi/" + src + "/0.jpg";
        }
    }
}
