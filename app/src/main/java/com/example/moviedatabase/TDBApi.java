package com.example.moviedatabase;/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-03-21
*/

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TDBApi {
    //
    @GET("3/trending/movie/day")
    Call<movieClass> getMovies(
            @Query("page") int page,
            @Query("api_key") String api_key
    );

    @GET("3/movie/now_playing")
    Call<movieClass> getCurrentMovies(
            @Query("page") int page,
            @Query("api_key") String api_key
    );

    @GET("3/movie/{movie_id}")
    Call<SingleMovie> getMovie(
            @Path("movie_id") String mid,
            @Query("api_key") String api_key
    );
}
