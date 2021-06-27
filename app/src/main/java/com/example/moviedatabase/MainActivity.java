package com.example.moviedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CustomAdapter.onMovieListener{

    List<Result> movieList;
    RecyclerView recyclerView;
    private ActionBar toolbar;
    CustomAdapter customAdapter;
    List<Result> allMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        toolbar = getSupportActionBar();
        setData(getIntent().getIntExtra("load",0));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        MenuItem menuItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println(newText);
                customAdapter.filter.filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setData(int t){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create()).build();
        TDBApi tdbApi = retrofit.create(TDBApi.class);
        String api_key = "be8c01d9e1cfee0ed6e48585dc405260";
        Call<movieClass> call;
        if(t == 0)
        {
            call = tdbApi.getMovies(1,api_key);
            toolbar.setTitle("Trending Movies");
        }
        else{
            call = tdbApi.getCurrentMovies(1,api_key);
            toolbar.setTitle("Now Playing");
        }


        call.enqueue(new Callback<movieClass>() {
            @Override
            public void onResponse(Call<movieClass> call, Response<movieClass> response) {

                movieClass details = response.body();
                movieList = details.getResults();
                allMovies = details.getResults();
                putDataIntoRecyclerView(movieList);
            }

            @Override
            public void onFailure(Call<movieClass> call, Throwable t) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_trending:
                    setData(0);
                    return true;
                case R.id.navigation_movies:
                    setData(1);
                    return true;
                case R.id.fav_movies:
                    toolbar.setTitle("My Favorites");
                    Intent intent = new Intent(getApplicationContext(),favMovies.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    private void putDataIntoRecyclerView(List<Result> movieList) {
        customAdapter = new CustomAdapter(this,movieList,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this,movieDetails.class);
        Result obj = movieList.get(position);
        intent.putExtra("movieDetails", obj);
        startActivity(intent);
    }
}