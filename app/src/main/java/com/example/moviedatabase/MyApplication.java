package com.example.moviedatabase;/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-03-21
*/

import android.app.Application;

import com.example.moviedatabase.dagger.DaggerRetroComponent;
import com.example.moviedatabase.dagger.RetroComponent;
import com.example.moviedatabase.dagger.RetroModule;

import dagger.internal.DaggerCollections;

public class MyApplication extends Application {
    private RetroComponent retroComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        retroComponent = DaggerRetroComponent.builder().retroModule(new RetroModule()).build();

    }
    public RetroComponent getRetroComponent(){
        return retroComponent;
    }
}
