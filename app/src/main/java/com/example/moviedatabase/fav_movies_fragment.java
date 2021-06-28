package com.example.moviedatabase;

/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class fav_movies_fragment extends Fragment implements FavCustomAdapter.onMovieListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView rc;
    List<movieModal> mmList;
    Context mContext;
    FavCustomAdapter favCustomAdapter;

    private String mParam1;
    private String mParam2;

    public fav_movies_fragment() {
        // Required empty public constructor
    }

    public static fav_movies_fragment newInstance(String param1, String param2) {
        fav_movies_fragment fragment = new fav_movies_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav_movies_fragment, container, false);
        rc = view.findViewById(R.id.recycler_view_favorite);
        getData();
        mContext = getActivity();
        setHasOptionsMenu(true);
        return view;
    }

    private void putDataIntoRecyclerView(List<movieModal> movieList) {
        movieDatabase md = movieDatabase.getInstance(getContext());
        favCustomAdapter = new FavCustomAdapter(getContext(),movieList,this);
        rc.setLayoutManager(new GridLayoutManager(getContext(),3));
        rc.setAdapter(favCustomAdapter);
    }

    void getData(){
        movieDatabase md = movieDatabase.getInstance(getContext());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Insert Data
                mmList = md.Dao().getAllFavs();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        putDataIntoRecyclerView(mmList);
                    }
                });

            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(getContext(),movieDetails.class);
        movieModal obj = mmList.get(position);
        intent.putExtra("movieModal", obj);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(favCustomAdapter != null){
            favCustomAdapter.filter.filter(newText);
            mmList = favCustomAdapter.getData();
        }

        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.mainmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }
}