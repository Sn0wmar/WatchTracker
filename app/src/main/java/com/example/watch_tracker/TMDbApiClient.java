package com.example.watch_tracker;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Classe implémentant un client API pour l'API The Movie Database, TMDb
public class TMDbApiClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/"; // URL de base de l'API TMDb
    private static final String API_KEY = "a5183ca5a42adb93356f8a7897bd6622"; // Clé d'API pour accéder à TMDb

    private static final String langage = "fr-FR";  // Langue utilisée dans les requêtes

    private static TMDbApiInterface tmDbApiInterface; // Interface pour les appels API

    // Méthode pour obtenir une instance de l'interface TMDbApiInterface
    public static TMDbApiInterface getApiClient() {
        if (tmDbApiInterface == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            tmDbApiInterface = retrofit.create(TMDbApiInterface.class);
        }
        return tmDbApiInterface;
    }
    // Méthode pour obtenir l'appel API pour récupérer le contenu tendance (films, séries, animés)
    public static Call<MovieResponse> getTrendingContent(int page) {
        return getApiClient().getTrendingContent(API_KEY, page,langage);
    }
}
