package com.example.watch_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.watch_tracker.MovieResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bouton extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private ConstraintLayout constraintLayout; // Ajout de la vue racine

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

        // Ajout de la vue racine
        constraintLayout = findViewById(R.id.your_root_layout_id);

        // Appel de la méthode pour obtenir les films populaires
        getPopularMovies();

        // Ajout de l'écouteur de changement de hauteur de fenêtre
        constraintLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            private int previousHeight;

            @Override
            public boolean onPreDraw() {
                int height = constraintLayout.getHeight();
                if (height != previousHeight) {
                    // La hauteur de la fenêtre a changé
                    boolean isKeyboardVisible = height < previousHeight;
                    if (isKeyboardVisible) {
                        // Le clavier est affiché, masquez la RecyclerView
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        // Le clavier n'est pas affiché, montrez la RecyclerView
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    previousHeight = height;
                }
                return true;
            }
        });
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

