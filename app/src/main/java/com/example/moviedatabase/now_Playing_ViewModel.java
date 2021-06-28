package com.example.moviedatabase;
/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class now_Playing_ViewModel extends AndroidViewModel {

    @Inject
    TDBApi tdbApi;
    private MutableLiveData<movieClass> liveDataList;

    public now_Playing_ViewModel(@NonNull Application application) {
        super(application);
        liveDataList = new MutableLiveData<>();
        ((MyApplication)application).getRetroComponent().injection(now_Playing_ViewModel.this);
    }

    public MutableLiveData<movieClass> getRecyclerListObserver()
    {
        return liveDataList;
    }

    public void makeApiCall(Context context){
        String api_key = "be8c01d9e1cfee0ed6e48585dc405260";
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        tdbApi.getCurrentMovies(1,countryCodeValue,api_key).toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<movieClass>() {
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
