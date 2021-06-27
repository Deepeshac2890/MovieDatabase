package com.example.moviedatabase;/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-03-21
*/

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class movieRepository {
    private Dao dao;
    private LiveData<List<movieModal>> allCourses;
    private List<movieModal> allFavMovies;
    boolean isFav;
    // creating a constructor for our variables
    // and passing the variables to it.
    public movieRepository(Application application) {
        movieDatabase database = movieDatabase.getInstance(application);
        dao = database.Dao();
        allCourses = dao.getAllMovies();
        allFavMovies = dao.getAllFavs();
    }



    // creating a method to insert the data to our database.
    public void insert(movieModal model) {
        new InsertMovieAsyncTask(dao).execute(model);
    }

    // creating a method to update data in database.
    public void update(movieModal model) {
        new UpdateMovieAsyncTask(dao).execute(model);
    }

    // creating a method to delete the data in our database.
    public void delete(movieModal model) {
        new DeleteMovieAsyncTask(dao).execute(model);
    }

    // below is the method to delete all the courses.
    public void deleteAllMovies() {
        new DeleteAllMoviesAsyncTask(dao).execute();
    }

    // below method is to read all the courses.
    public LiveData<List<movieModal>> getAllMovies() {
        return allCourses;
    }

    public List<movieModal> getAllFavMovies() {
        return allFavMovies;
    }

    public boolean isFav(){
        return isFav;
    }

    // we are creating a async task method to insert new course.
    private static class InsertMovieAsyncTask extends AsyncTask<movieModal, Void, Void> {
        private Dao dao;

        private InsertMovieAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(movieModal... model) {
            // below line is use to insert our modal in dao.
            dao.insert(model[0]);
            return null;
        }
    }

    // we are creating a async task method to update our course.
    private static class UpdateMovieAsyncTask extends AsyncTask<movieModal, Void, Void> {
        private Dao dao;

        private UpdateMovieAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(movieModal... models) {
            // below line is use to update
            // our modal in dao.
            dao.update(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete course.
    private static class DeleteMovieAsyncTask extends AsyncTask<movieModal, Void, Void> {
        private Dao dao;

        private DeleteMovieAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(movieModal... models) {
            // below line is use to delete
            // our course modal in dao.
            dao.delete(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all courses.
    private static class DeleteAllMoviesAsyncTask extends AsyncTask<Void, Void, Void> {
        private Dao dao;
        private DeleteAllMoviesAsyncTask(Dao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method
            // to delete all courses.
            dao.deleteAllMovies();
            return null;
        }
    }
}
