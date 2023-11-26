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

        // détection d'appui sur les boutons
        ImageView mask = findViewById(R.id.pas_vu);
        ImageView mask2 = findViewById(R.id.en_cours);
        ImageView mask3 = findViewById(R.id.vu);
        ImageView mask4 = findViewById(R.id.profil);
        ImageView mask5 = findViewById(R.id.liste);
        ImageView mask7 = findViewById(R.id.bouton_plus);

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

        //initialisation rv
        recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();
        rvAdapter = new RVAdapter(this, movieList, this);
        recyclerView.setAdapter(rvAdapter);

        constraintLayout = findViewById(R.id.encours);

        // champ de recherche
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


        searchField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Cacher le clavier si user n'ecris pas
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });


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

        // charge les films
        loadMovies();

        constraintLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            private int previousHeight;
            // retire la rv quand le clavier est affiche a l'ecran
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

    private void loadMovies() {
        //recupere info user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            //recupere id user
            String userId = currentUser.getUid();

            // recupere les films de user
            DatabaseReference userMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("movies");
            userMoviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // vide la liste
                    movieList.clear();
                    for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                        // regarde tous les films de user
                        Movie movie = movieSnapshot.getValue(Movie.class);
                        if (movie != null && "En cours".equals(movie.getStatut())) { // si different de nul et à le statut "En cours"
                            movieList.add(movie); //on ajoute a la liste de film
                        }
                    }
                    rvAdapter.notifyDataSetChanged(); // afficher la liste
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Gestion des erreurs de base de données
                }
            });
        }
    }

    private void performSearch(String query) {
        loadMovies(query);
    }

    private void loadMovies(String query) {
        // recup info user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            //recup id user
            String userId = currentUser.getUid();
           //  recherche dans la firebase le contenu correspondant a ce que user à ecris
            DatabaseReference userMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("movies");
            Query searchQuery = userMoviesRef.orderByChild("title").startAt(query).endAt(query + "\uf8ff");

            searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    movieList.clear(); // vide la liste
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
                    // Gestion des erreurs de base de données
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

    // Fonction pour masquer le clavier
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
        }
    }
}
