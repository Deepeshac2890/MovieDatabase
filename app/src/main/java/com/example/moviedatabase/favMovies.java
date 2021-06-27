package com.example.moviedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class favMovies extends AppCompatActivity implements FavCustomAdapter.onMovieListener{

    RecyclerView rc;
    List<movieModal> mmList;
    ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_movies);
        rc = findViewById(R.id.fav_recycler_view);
        toolbar = getSupportActionBar();
        toolbar.setTitle("My Favorites");
        mmList = new ArrayList<>();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view_fav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getData();
    }
    private void putDataIntoRecyclerView(List<movieModal> movieList) {
        System.out.print(movieList.size());
        FavCustomAdapter customAdapter = new FavCustomAdapter(this,movieList,this);
        rc.setLayoutManager(new GridLayoutManager(this,3));
        rc.setAdapter(customAdapter);
    }

    void getData(){
        movieDatabase md = movieDatabase.getInstance(this.getApplicationContext());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Insert Data
                mmList = md.Dao().getAllFavs();
                putDataIntoRecyclerView(mmList);
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        System.out.print(position + " Here It Clicked");
        Intent intent = new Intent(this,movieDetails.class);
        movieModal obj = mmList.get(position);
        intent.putExtra("movieModal", obj);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_trending:
                    intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("load",0);
                    startActivity(intent);
                    return true;
                case R.id.navigation_movies:
                    intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("load",1);
                    startActivity(intent);
                    return true;
                case R.id.fav_movies:
                    intent = new Intent(getApplicationContext(),favMovies.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
}