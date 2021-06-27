package com.example.moviedatabase;/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModal {
    private movieRepository repository;

    // below line is to create a variable for live
    // data where all the courses are present.
    private LiveData<List<movieModal>> allmovies;

    // constructor for our view modal.
    public ViewModal(@NonNull Application application) {
        super();
        repository = new movieRepository(application);
        allmovies = repository.getAllMovies();
    }

    // below method is use to insert the data to our repository.
    public void insert(movieModal model) {
        repository.insert(model);
    }

    // below line is to update data in our repository.
    public void update(movieModal model) {
        repository.update(model);
    }

    // below line is to delete the data in our repository.
    public void delete(movieModal model) {
        repository.delete(model);
    }

    // below method is to delete all the courses in our list.
    public void deleteAllmovies() {
        repository.deleteAllMovies();
    }

    // below method is to get all the courses in our list.
    public LiveData<List<movieModal>> getAllmovies() {
        return allmovies;
    }
}
