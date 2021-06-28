package com.example.moviedatabase;

/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{

    private ActionBar toolbar;
    Fragment trending_frag;
    Fragment now_frag;
    Fragment fav_frag;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();
        trending_frag = new trending_movies_frag();
        now_frag = new nowPlayingFrag();
        fav_frag = new fav_movies_fragment();

        selectFragment(getIntent().getIntExtra("load",0));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    void selectFragment(int opt){
        if(opt == 0)
        {
            toolbar.setTitle("Trending Movies");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHome,trending_frag).commit();
        }else if(opt == 1){
            toolbar.setTitle("Now Playing");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHome,now_frag).commit();
        }
        else
        {
            toolbar.setTitle("My Favorites");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHome,fav_frag).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_trending:
                   selectFragment(0);
                    return true;
                case R.id.navigation_movies:
                   selectFragment(1);
                    return true;
                case R.id.fav_movies:
                    selectFragment(2);
                    return true;
            }
            return false;
        }
    };
}