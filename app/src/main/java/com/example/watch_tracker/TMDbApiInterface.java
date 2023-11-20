package com.example.watch_tracker;

import com.example.watch_tracker.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApiInterface {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String cleApi,
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("movie/{id}")
    Call<MovieResponse> getMovieDetails(
            @Path("id") int idFilm,
            @Query("api_key") String cleApi,
            @Query("language") String language
    );
}
