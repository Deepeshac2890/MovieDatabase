package com.example.moviedatabase;
/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

import android.app.Application;

import com.example.moviedatabase.dagger.DaggerRetroComponent;
import com.example.moviedatabase.dagger.RetroComponent;
import com.example.moviedatabase.dagger.RetroModule;

public class MyApplication extends Application {
    private RetroComponent retroComponent;
    @Override
    public void onCreate() {
        retroComponent = DaggerRetroComponent.builder().retroModule(new RetroModule()).build();
        super.onCreate();
    }
    public RetroComponent getRetroComponent(){
        return retroComponent;
    }
}
