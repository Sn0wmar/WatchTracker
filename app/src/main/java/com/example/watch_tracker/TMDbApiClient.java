package com.example.watch_tracker;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDbApiClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "a5183ca5a42adb93356f8a7897bd6622";

    private static final String langage = "fr-FR";

    private static TMDbApiInterface tmDbApiInterface;

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

    public static Call<MovieResponse> getTrendingContent(int page) {
        return getApiClient().getTrendingContent(API_KEY, page,langage);
    }
}
