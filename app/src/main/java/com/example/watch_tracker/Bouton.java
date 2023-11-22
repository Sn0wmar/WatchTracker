package com.example.watch_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
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

        getTrendingContent();

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
                // Ne rien faire ici
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Ne rien faire ici
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Appel à la méthode performSearch lorsque le texte change
                performSearch(editable.toString());
            }
        });
    }

    private void getTrendingContent() {
        Call<MovieResponse> call = TMDbApiClient.getTrendingContent(1);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
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
        Call<MovieResponse> call = TMDbApiClient.getApiClient().searchAllContent(API_KEY, query, langage, 1);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
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
        Intent intent = new Intent(Bouton.this, FilmDetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}
