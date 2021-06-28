package com.example.moviedatabase;

/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao
{
    // below method is use to
    // add data to database.
    @Insert
    void insert(movieModal model);

    // below method is use to update
    // the data in our database.
    @Update
    void update(movieModal model);

    // below line is use to delete a
    // specific course in our database.
    @Delete
    void delete(movieModal model);

    // on below line we are making query to
    // delete all courses from our database.
    @Query("DELETE FROM movie_table")
    void deleteAllMovies();

    // below line is to read all the courses from our database.
    // in this we are ordering our courses in ascending order
    // with our course name.
    @Query("SELECT * FROM movie_table ORDER BY movieTitle ASC")
    LiveData<List<movieModal>> getAllMovies();

    @Query("SELECT * FROM movie_table where isFavourite")
    List<movieModal> getAllFavs();

    @Query("SELECT isFavourite FROM movie_table where movieTitle = :title")
    boolean isFav(String title);

    @Query("SELECT EXISTS(SELECT * from movie_table WHERE movieTitle= :title)")
    int isExist(String title);

    // This is for Searching
    @Query("SELECT * FROM movie_table WHERE movieTitle LIKE :text")
    List<movieModal> getSearchResults(String text);
}
