package com.example.moviedatabase;
/*
Created By: Deepesh Acharya
Maintained By: Deepesh Acharya
Latest Version Date : 27-06-21
*/

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TDBApi {
    //
    @GET("3/trending/movie/day")
    Flowable<movieClass> getMovies(
            @Query("page") int page,
            @Query("api_key") String api_key
    );

    @GET("3/movie/now_playing")
    Flowable<movieClass> getCurrentMovies(
            @Query("page") int page,
            @Query("region") String code,
            @Query("api_key") String api_key
    );

    @GET("3/movie/{movie_id}")
    Flowable<SingleMovie> getMovie(
            @Path("movie_id") String mid,
            @Query("api_key") String api_key
    );
}
