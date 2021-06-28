package com.example.moviedatabase.dagger;/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-03-21
*/

import com.example.moviedatabase.now_Playing_ViewModel;
import com.example.moviedatabase.trending_ViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RetroModule.class})
public interface RetroComponent {

    public void inject(trending_ViewModel trendingViewModel);

    public void injection(now_Playing_ViewModel now_playing_viewModel);
}
