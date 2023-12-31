package com.example.watch_tracker;

import com.example.watch_tracker.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//Interface décrivant les points d'accès de l'API TMDb
public interface TMDbApiInterface {

    //Méthode pour récupérer le contenu tendance de la semaine
    @GET("trending/all/week")
    Call<MovieResponse> getTrendingContent(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("language") String language
    );

    //Méthode pour récupérer les détails d'un film ou d'une série spécifique
    @GET("media/{id}")
    Call<MovieResponse> getMovieDetails(
            @Path("id") int idFilm,
            @Query("api_key") String cleApi,
            @Query("language") String language
    );

    //Méthode pour effectuer une recherche de contenu multi-catégorie
    @GET("search/multi")
    Call<MovieResponse> searchAllContent(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("language") String language,
            @Query("page") int page
    );
}
