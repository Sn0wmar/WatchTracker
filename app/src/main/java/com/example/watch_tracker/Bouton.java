package com.example.watch_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

        ImageView mask = findViewById(R.id.pas_vu);
        ImageView mask2 = findViewById(R.id.en_cours);
        ImageView mask3 = findViewById(R.id.vu);
        ImageView mask4 = findViewById(R.id.profil);
        ImageView mask5 = findViewById(R.id.liste);


        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), Pas_visionne.class);
                startActivity(it);
            }
        });

        mask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), en_cours.class);
                startActivity(it);
            }
        });

        mask3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), visionne.class);
                startActivity(it);

            }
        });

        mask4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), profil.class);
                startActivity(it);
            }
        });

        mask5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), Liste_personnelle.class);
                startActivity(it);

            }
        });










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
