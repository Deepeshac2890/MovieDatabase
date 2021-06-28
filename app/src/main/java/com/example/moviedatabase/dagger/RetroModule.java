package com.example.moviedatabase.dagger;/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-03-21
*/

import com.example.moviedatabase.TDBApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetroModule {
    String baseUrl = "https://api.themoviedb.org";

    @Provides
    @Singleton
    public Retrofit getRetrofitInstance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
        return  retrofit;
    }

    @Provides
    @Singleton
    public TDBApi getTDBApiService(Retrofit retrofit){
        return retrofit.create(TDBApi.class);
    }
}
