package com.example.moviedatabase;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

            int mid = Integer.valueOf(id);
            Toast.makeText(this, "Path : " + mid, Toast.LENGTH_SHORT).show();
            getMovieDeepLink(mid);
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

    private void getMovieDeepLink(int t){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
        TDBApi tdbApi = retrofit.create(TDBApi.class);
        String api_key = "be8c01d9e1cfee0ed6e48585dc405260";

        tdbApi.getMovie(String.valueOf(t),api_key).toObservable().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(new Observer<SingleMovie>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull SingleMovie singleMovie) {
                Toast.makeText(getApplicationContext(),"Data Came : " + singleMovie.getOriginalTitle(),Toast.LENGTH_LONG).show();
                SingleMovie rs = singleMovie;
                rating.setText(rs.getVoteAverage().toString());
                overview.setText(rs.getOverview());
                releaseDate.setText(rs.getReleaseDate());
                Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w185" + rs.getPosterPath()).into(img);
                title.setText(rs.getTitle());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });

//        call.enqueue(new Callback<SingleMovie>() {
//            @Override
//            public void onResponse(Call<SingleMovie> call, Response<SingleMovie> response) {
//               // Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
//                SingleMovie rs = response.body();
//                rating.setText(rs.getVoteAverage().toString());
//                overview.setText(rs.getOverview());
//                releaseDate.setText(rs.getReleaseDate());
//                Glide.with(getApplicationContext()).load("https://image.tmdb.org/t/p/w185" + rs.getPosterPath()).into(img);
//                title.setText(rs.getTitle());
//            }
//
//            @Override
//            public void onFailure(Call<SingleMovie> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();
//            }
//        });
    }


    public void favClicked(View view) {
        movieDatabase md = movieDatabase.getInstance(this.getApplicationContext());
        movieModal mm;
        boolean isFav;
        System.out.println("The TG VALUE UPPER IS : " + tg.isChecked() + tgValue);
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

        System.out.println("The TG VALUE IS : " + tg.isChecked());
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
        ApplicationInfo api = getApplicationContext().getApplicationInfo();
        String apkPath = api.sourceDir;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
//        String title;
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