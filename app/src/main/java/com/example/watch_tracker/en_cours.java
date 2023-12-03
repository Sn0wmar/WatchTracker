package com.example.watch_tracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class en_cours extends AppCompatActivity implements RVAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private List<Movie> movieList;
    private ConstraintLayout constraintLayout;
    private EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.en_cours);

        //Détection d'appui sur les boutons
        ImageView mask = findViewById(R.id.pas_vu);
        ImageView mask2 = findViewById(R.id.en_cours);
        ImageView mask3 = findViewById(R.id.vu);
        ImageView mask4 = findViewById(R.id.profil);
        ImageView mask5 = findViewById(R.id.liste);
        ImageView mask7 = findViewById(R.id.bouton_plus);

        //Redirection vs différentes activités en fonction des boutons cliqués
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

        mask7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), Bouton.class);
                startActivity(it);
            }
        });

        //Initialisation de la RecyclerView
        recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();
        rvAdapter = new RVAdapter(this, movieList, this);
        recyclerView.setAdapter(rvAdapter);

        constraintLayout = findViewById(R.id.encours);

        //Champ de recherche
        searchField = findViewById(R.id.searchField);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                performSearch(editable.toString());
            }
        });


        //Gestion des actions liées au clavier lors de la recherche
        searchField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //Cacher le clavier si l'utilisateur n'écrit pas
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });


        //Gestion des actions liées au clavier lors de la recherche
        searchField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // Masquer le clavier quand user clique sur retour
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        //Chargement des films en cours
        loadMovies();

        constraintLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            private int previousHeight;
            //Retire la RecyclerView quand le clavier est affiché à l'écran
            @Override
            public boolean onPreDraw() {
                int height = constraintLayout.getHeight();
                if (height != previousHeight) {
                    boolean isKeyboardVisible = height < previousHeight;
                    if (isKeyboardVisible) {
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    previousHeight = height;
                }
                return true;
            }
        });
    }

    //Chargement des films en cours depuis la base de donées Firebase
    private void loadMovies() {
        //recupere info user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            //Récupère id user
            String userId = currentUser.getUid();

            //Récupère les films de user
            DatabaseReference userMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("movies");
            userMoviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Vide la liste
                    movieList.clear();
                    for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                        //Regarde tout les films de user
                        Movie movie = movieSnapshot.getValue(Movie.class);
                        if (movie != null && "En cours".equals(movie.getStatut())) { //Si différent de nul et au statut "En cours"
                            movieList.add(movie); //On ajoute à la liste de film
                        }
                    }
                    rvAdapter.notifyDataSetChanged(); //Affiche la liste
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Gestion des erreurs de base de données
                }
            });
        }
    }

    //Effectue la recherche des films en cours en fonction de la requête de l'user
    private void performSearch(String query) {
        loadMovies(query);
    }

    //Effectue la recherche des films en cours en fonction de la requête de l'user
    private void loadMovies(String query) {
        //Récupère info user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            //Récupère id user
            String userId = currentUser.getUid();
           //Recherche dans la firebase le contenu correspondant à ce que user à écrit
            DatabaseReference userMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("movies");
            Query searchQuery = userMoviesRef.orderByChild("title").startAt(query).endAt(query + "\uf8ff");

            searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    movieList.clear(); //Vide la liste
                    for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                        Movie movie = movieSnapshot.getValue(Movie.class);
                        if (movie != null && "En cours".equals(movie.getStatut())) {
                            movieList.add(movie);
                        }
                    }
                    rvAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Gestion des erreurs de base de données
                }
            });
        }
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(en_cours.this, FilmDetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    //Fonction pour masquer le clavier
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
        }
    }
}
