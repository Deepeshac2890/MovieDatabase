package com.example.moviedatabase;

/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

import android.app.Activity;
import android.app.TaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class trending_movies_frag extends Fragment implements CustomAdapter.onMovieListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private trending_ViewModel trending_viewModel;
    RecyclerView rc;
    List<Result> movieList;
    public CustomAdapter customAdapter;
    List<Result> allMovies;
    Context mContext;
    TextView last_updated;
    String formattedDate;

    private String mParam1;
    private String mParam2;

    public trending_movies_frag() {
        // Required empty public constructor
    }

    public static trending_movies_frag newInstance(String param1, String param2) {
        trending_movies_frag fragment = new trending_movies_frag();
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
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_trending_movies_frag, container, false);

        rc = view.findViewById(R.id.recycler_view_trending);
        last_updated = view.findViewById(R.id.last_update_trending);

        setData();
        getCurrentDate();

        return view;
    }

    void getCurrentDate(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);

    }

    void setData(){
        if(isInternetWorking())
        {
            // Get from Internet
            last_updated.setText("Last Updated On : " + formattedDate);
            trending_viewModel = new ViewModelProvider(this).get(trending_ViewModel.class);
            trending_viewModel.getRecyclerListObserver().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<movieClass>() {
                @Override
                public void onChanged(movieClass movieClass) {
                    if(movieClass != null)
                    {
                        movieClass details = movieClass;
                        movieList = details.getResults();
                        allMovies = details.getResults();
                        updateDB(movieList);
                        putDataIntoRecyclerView(movieList);
                    }
                }
            });
            trending_viewModel.makeApiCall();
        }
        else {
            // Get from DB
            getFromDB();
        }
    }

    public void getFromDB(){
      List<Result> results = new ArrayList<>();
      movieDatabase md = movieDatabase.getInstance(getContext());

      AsyncTask.execute(new Runnable() {
          @Override
          public void run() {
            List<movieModal> list = md.Dao().getAllTrending();
            for(movieModal movie : list)
            {
                formattedDate = movie.getLastUpdatedTrending();
                Result rs = new Result(movie.getPosterPath(),Double.parseDouble(movie.getRating()),movie.getMovieOverview(),movie.getReleaseDate(),movie.getId(),movie.getMovieTitle());
                results.add(rs);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    movieList = new ArrayList<>();
                    movieList.addAll(results);
                    putDataIntoRecyclerView(results);
                }
            });
          }
      });
      last_updated.setText("Last Updated On : " + formattedDate);
    }

    public boolean isInternetWorking() {
        final boolean[] success = new boolean[1];
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    try {
                        URL url = new URL("https://google.com");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(10000);
                        connection.connect();
                        success[0] = connection.getResponseCode() == 200;
                    } catch (IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"Not Connected to Internet",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return success[0];
    }

    private void putDataIntoRecyclerView(List<Result> movieList) {
        customAdapter = new CustomAdapter(getContext(),movieList,this::onMovieClick);
        rc.setLayoutManager(new GridLayoutManager(getContext(),3));
        rc.setAdapter(customAdapter);
    }

    public void updateDB(List<Result> movies)
    {
        movieDatabase md = movieDatabase.getInstance(getContext());

        // Clear the Database
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                md.Dao().deleteTrendingMovies();
            }
        });

        // Insert into DB
        for(Result movie: movies)
        {
            final boolean[] isFav = {false};
            movieModal mm = new movieModal(movie.getTitle(),movie.getOverview(),movie.getPosterPath(),movie.getVoteAverage().toString(),movie.getReleaseDate());
            mm.setId(movie.getId());
            mm.setTrending(true);
            mm.setLastUpdatedTrending(formattedDate);
            final int[] isExist = new int[1];

           Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    isExist[0] = md.Dao().isExist(mm.getMovieTitle());
                    if(isExist[0] == 1)
                    {
                        isFav[0] = md.Dao().isItFav(mm.getMovieTitle());
                    }
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    if (isExist[0] == 0)
                    {
                        // Insert Data
                        md.Dao().insert(mm);
                    }
                    else
                    {
//                        Update Data
                        mm.setFavourite(isFav[0]);
                        md.Dao().update(mm);
                    }
                }
            });
        }
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(getContext(),movieDetails.class);
        Result obj = movieList.get(position);
        intent.putExtra("movieDetails", obj);
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
        if(customAdapter != null)
        {
            customAdapter.filter.filter(newText);
            movieList = customAdapter.getData();
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
