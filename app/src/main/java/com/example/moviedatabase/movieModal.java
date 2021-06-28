package com.example.moviedatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

@Entity(tableName = "movie_table")
public class movieModal implements Serializable {
    @PrimaryKey(autoGenerate = true)

    // variable for our id.
    private int id;

    // below line is a variable
    // for course name.
    private String movieTitle;

    // below line is use for
    // course description.
    private String movieOverview;

    // below line is use
    // for course duration.
    private String posterPath;

    private String rating;

    private String releaseDate;

    private boolean isFavourite;

    private boolean isTrending;

    private boolean isNowPlaying;

    private String lastUpdatedTrending;

    private String lastUpdatedNowPlaying;

    public String getLastUpdatedTrending() {
        return lastUpdatedTrending;
    }

    public void setLastUpdatedTrending(String lastUpdatedTrending) {
        this.lastUpdatedTrending = lastUpdatedTrending;
    }

    public String getLastUpdatedNowPlaying() {
        return lastUpdatedNowPlaying;
    }

    public void setLastUpdatedNowPlaying(String lastUpdatedNowPlaying) {
        this.lastUpdatedNowPlaying = lastUpdatedNowPlaying;
    }

    public boolean isNowPlaying() {
        return isNowPlaying;
    }

    public void setNowPlaying(boolean nowPlaying) {
        isNowPlaying = nowPlaying;
    }

    public boolean isTrending() {
        return isTrending;
    }

    public void setTrending(boolean trending) {
        isTrending = trending;
    }

    public movieModal(String movieTitle, String movieOverview, String posterPath, String rating, String releaseDate) {
        this.movieTitle = movieTitle;
        this.movieOverview = movieOverview;
        this.posterPath = posterPath;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
