package com.example.moviedatabase;/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-03-21
*/

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class trending_ViewModel extends AndroidViewModel {


    @Inject
    TDBApi tdbApi;
    private MutableLiveData<movieClass> liveDataList;


    public trending_ViewModel(@NonNull Application application) {
        super(application);
        liveDataList = new MutableLiveData<>();
        ((MyApplication)application).getRetroComponent().inject(trending_ViewModel.this);
    }

    public MutableLiveData<movieClass> getRecyclerListObserver()
    {
        return liveDataList;
    }

    public void makeApiCall(){
        String api_key = "be8c01d9e1cfee0ed6e48585dc405260";
        tdbApi.getMovies(1,api_key).toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<movieClass>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull movieClass movieClass) {
                liveDataList.postValue(movieClass);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
