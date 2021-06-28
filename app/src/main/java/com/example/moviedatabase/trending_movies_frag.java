package com.example.moviedatabase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class trending_movies_frag extends Fragment implements CustomAdapter.onMovieListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private trending_ViewModel trending_viewModel;
    RecyclerView rc;
    List<Result> movieList;
    public CustomAdapter customAdapter;
    List<Result> allMovies;
    Context mContext;

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
        setData();
        return view;
    }
    void setData(){
        trending_viewModel = new ViewModelProvider(this).get(trending_ViewModel.class);
        trending_viewModel.getRecyclerListObserver().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<movieClass>() {
            @Override
            public void onChanged(movieClass movieClass) {
                if(movieClass != null)
                {
                    movieClass details = movieClass;
                    movieList = details.getResults();
                    allMovies = details.getResults();
                    putDataIntoRecyclerView(movieList);
                }
            }
        });
        trending_viewModel.makeApiCall();
    }

//    void getData(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.themoviedb.org")
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create()).build();
//        TDBApi tdbApi = retrofit.create(TDBApi.class);
//        String api_key = "be8c01d9e1cfee0ed6e48585dc405260";
//        tdbApi.getMovies(1,api_key).toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<movieClass>() {
//                @Override
//                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
//
//                }
//
//                @Override
//                public void onNext(@io.reactivex.annotations.NonNull movieClass movieClass) {
//                    movieClass details = movieClass;
//                    movieList = details.getResults();
//                    allMovies = details.getResults();
//                    putDataIntoRecyclerView(movieList);
//                }
//
//                @Override
//                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            });
//
//    }

    private void putDataIntoRecyclerView(List<Result> movieList) {
        customAdapter = new CustomAdapter(getContext(),movieList,this::onMovieClick);
        rc.setLayoutManager(new GridLayoutManager(getContext(),3));
        rc.setAdapter(customAdapter);
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
            customAdapter.filter.filter(newText);
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