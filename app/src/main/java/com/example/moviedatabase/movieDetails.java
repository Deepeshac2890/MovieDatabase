package com.example.moviedatabase;

/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

public class movieDetails extends AppCompatActivity {
    Result selectedMovie;
    movieModal selectedModal;
    ToggleButton tg;
    boolean tgValue;
    TextView overview;
    TextView rating;
    TextView releaseDate;
    TextView title;
    ImageView img;
    movie_details_ViewModel movie_details_viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        tg = findViewById(R.id.fav);
        img = findViewById(R.id.iv_movie_poster);
        title =  findViewById(R.id.movie_title);
        releaseDate = findViewById(R.id.movie_release_date);
        rating = findViewById(R.id.movie_rating);
        overview = findViewById(R.id.movie_overview);

        Uri uri = getIntent().getData();
        if(uri != null)
        {
            // Hey Checkout this Awesome Movie : https:moviedashboard.com/
            String path = uri.toString();
            String id = path.substring(27);

//            int mid = Integer.valueOf(id);
            getMovieDetailsDeepLink(id);
        }
        else
        {
            selectedMovie = (Result) getIntent().getSerializableExtra("movieDetails");
            selectedModal = (movieModal) getIntent().getSerializableExtra("movieModal");
            if(selectedModal != null)
            {
                rating.setText(selectedModal.getRating().toString());
                overview.setText(selectedModal.getMovieOverview());
                releaseDate.setText(selectedModal.getReleaseDate());
                Glide.with(this).load("https://image.tmdb.org/t/p/w185" + selectedModal.getPosterPath()).into(img);
                title.setText(selectedModal.getMovieTitle());
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        movieDatabase md = movieDatabase.getInstance(getApplicationContext());
                        tgValue =md.Dao().isFav(selectedModal.getMovieTitle());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tg.setChecked(tgValue);
                                tg.setEnabled(true);
                            }
                        });
                    }
                });
            }
            else
            {
                rating.setText(selectedMovie.getVoteAverage().toString());
                overview.setText(selectedMovie.getOverview());

                releaseDate.setText(selectedMovie.getReleaseDate());
                Glide.with(this).load("https://image.tmdb.org/t/p/w185" + selectedMovie.getPosterPath()).into(img);
                title.setText(selectedMovie.getTitle());
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        movieDatabase md = movieDatabase.getInstance(getApplicationContext());
                        tgValue =md.Dao().isFav(selectedMovie.getTitle());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tg.setChecked(tgValue);
                                tg.setEnabled(true);
                            }
                        });
                    }
                });
            }
        }
    }

    void getMovieDetailsDeepLink(String movieID){
        String img_base_url ="https://image.tmdb.org/t/p/w185";
        movie_details_viewModel = new ViewModelProvider(this).get(movie_details_ViewModel.class);
        movie_details_viewModel.getRecyclerListObserver().observe(this, new androidx.lifecycle.Observer<SingleMovie>() {
            @Override
            public void onChanged(SingleMovie singleMovie) {
                if(singleMovie != null)
                {
                    SingleMovie rs = singleMovie;
                    rating.setText(rs.getVoteAverage().toString());
                    overview.setText(rs.getOverview());
                    releaseDate.setText(rs.getReleaseDate());
                    Glide.with(getApplicationContext()).load( img_base_url + rs.getPosterPath()).into(img);
                    title.setText(rs.getTitle());
                }
            }
        });

        movie_details_viewModel.makeApiCall(movieID);
    }

    public void favClicked(View view) {
        movieDatabase md = movieDatabase.getInstance(this.getApplicationContext());
        movieModal mm;
        if(selectedMovie != null)
        {
            mm = new movieModal(selectedMovie.getTitle(),selectedMovie.getOverview(),selectedMovie.getPosterPath(),selectedMovie.getVoteAverage().toString(),selectedMovie.getReleaseDate());
            mm.setId(selectedMovie.getId());
        }
        else
        {
            mm = selectedModal;
            mm.setId(selectedModal.getId());
        }

        if(tgValue){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    mm.setFavourite(false);
                    md.Dao().update(mm);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tg.setChecked(false);
                            tgValue = false;
                        }
                    });
                }
            });

        }
        else
            {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        int i = md.Dao().isExist(mm.getMovieTitle());
                        mm.setFavourite(true);
                        if (i == 0)
                        {
                            // Insert Data
                            md.Dao().insert(mm);
                        }
                        else
                        {
                            // Update Data
                            md.Dao().update(mm);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tg.setChecked(true);
                                tgValue = true;
                            }
                        });
                    }
                });
        }

    }

    public void share(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        int id;
        if(selectedMovie != null)
        {
            id = selectedMovie.getId();
        }
        else
        {
            id = selectedModal.getId();
        }

        String url = "Hey Checkout this Awesome Movie : https://moviedashboard.com/"+id;
        String sub = "Movie Dashboard";
        intent.putExtra(Intent.EXTRA_SUBJECT,sub);
        intent.putExtra(Intent.EXTRA_TEXT,url);
        startActivity(Intent.createChooser(intent,"Share Via"));
    }
}