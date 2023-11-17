package com.example.watch_tracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.watch_tracker.Movie;
import com.example.watch_tracker.MovieResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bouton extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bouton);

        // Initialisation de la RecyclerView
        recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Appel de la méthode pour obtenir les films populaires
        getPopularMovies();


    }

    private void getPopularMovies() {
        Call<MovieResponse> call = TMDbApiClient.getPopularMovies(1); // Vous pouvez ajuster la page si nécessaire

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Mettez à jour la RecyclerView avec les films populaires
                    rvAdapter = new RVAdapter(Bouton.this, response.body().getResults());
                    recyclerView.setAdapter(rvAdapter);

                } else {
                    Log.e("Bouton", "Erreur de réponse : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("Bouton", "Erreur de requête : " + t.getMessage());
            }
        });
    }
}
