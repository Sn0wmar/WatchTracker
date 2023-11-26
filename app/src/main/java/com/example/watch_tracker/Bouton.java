package com.example.watch_tracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.watch_tracker.Movie;
import com.example.watch_tracker.MovieResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bouton extends AppCompatActivity implements RVAdapter.OnItemClickListener {

    private static final String API_KEY = "a5183ca5a42adb93356f8a7897bd6622";
    private static final String langage = "fr-FR";

    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private ConstraintLayout constraintLayout;
    private EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bouton);

        // Initialisation de la RecyclerView
        recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        constraintLayout = findViewById(R.id.Bouton);
        searchField = findViewById(R.id.searchField);

        // détection d'appui sur les boutons
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


        searchField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // Cacher le clavier si user n'ecris pas
                    hideKeyboard();
                }
            }
        });


        searchField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH) {
                    // Cacher le clavier si la touche "Done" est pressée
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });
        // recupere les contenus populaires
        getTrendingContent();

        // retire la recyclerview si le clavier est affiche a l'ecran
        constraintLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            private int previousHeight;

            @Override
            public boolean onPreDraw() {
                int height = constraintLayout.getHeight();
                if (height != previousHeight) {
                    boolean isKeyboardVisible = height < previousHeight;
                    recyclerView.setVisibility(isKeyboardVisible ? View.GONE : View.VISIBLE);
                    previousHeight = height;
                }
                return true;
            }
        });



        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // rien faire ici
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // rien faire ici
            }

            @Override
            public void afterTextChanged(Editable editable) {
                performSearch(editable.toString());
            }
        });
    }

    private void getTrendingContent() {

        Call<MovieResponse> call = TMDbApiClient.getTrendingContent(1); // demande a l'api de recuperer la page 1 des contenus populaires
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) { // si on a recuperer on affiche
                    rvAdapter = new RVAdapter(Bouton.this, response.body().getResults(), Bouton.this);
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

    private void performSearch(String query) {
        //on recherhe le contenu demande par user
        Call<MovieResponse> call = TMDbApiClient.getApiClient().searchAllContent(API_KEY, query, langage, 1);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) { //si on a un resultat on affiche
                    rvAdapter = new RVAdapter(Bouton.this, response.body().getResults(), Bouton.this);
                    recyclerView.setAdapter(rvAdapter);
                } else {
                    Log.e("Bouton", "Erreur de réponse de la recherche : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("Bouton", "Erreur de requête de recherche : " + t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(Movie movie) {
        // si on clique sur un film on afffiche les details de celui-ci
        Intent intent = new Intent(Bouton.this, FilmDetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    // methode pour cacher le clavier
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
