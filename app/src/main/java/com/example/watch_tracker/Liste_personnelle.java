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

//Déclaration de le classe Liste_personnelle
public class Liste_personnelle extends AppCompatActivity implements RVAdapter.OnItemClickListener {

    //Déclaration des variables
    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private List<Movie> movieList;
    private ConstraintLayout constraintLayout;
    private EditText searchField;


    //M2thode appelée lors de la création de l'activité
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_personnelle);

        //Définition des références des boutons
        ImageView mask = findViewById(R.id.pas_vu_2);
        ImageView mask2 = findViewById(R.id.en_cours);
        ImageView mask3 = findViewById(R.id.vu);
        ImageView mask4 = findViewById(R.id.profil);
        ImageView mask5 = findViewById(R.id.liste);
        ImageView mask7 = findViewById(R.id.bouton_plus);


        //Définiton des écouteurs de clic pour les boutons de navigation


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

        //Récupération de la ConstraintLayout
        constraintLayout = findViewById(R.id.listeperso);

        //Récupération du champ de recherche et ajout d'écouteurs
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
                    //Masquer le clavier
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
                    //Masquer le clavier
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        //Chargement des films
        loadMovies();

        //Ajout d'un écouteur pour détecter la visibilité du clavier virtuel
        constraintLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            private int previousHeight;

            @Override
            public boolean onPreDraw() { // si le clavier est affiche la rv disparait
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

    //Méthode pour charger tous les films de l'utilisateur connecté
    private void loadMovies() { // affiche tous les films de l'utilisateur connecté
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("movies");
            userMoviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    movieList.clear();
                    for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                        Movie movie = movieSnapshot.getValue(Movie.class);
                        if (movie != null) {
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

    //Méthode pour effectuer une recherche
    private void performSearch(String query) { // fait une recherche
        loadMovies(query);
    }

    //Méthode pour charger les films correspondant à la recherche dans Firebase
    private void loadMovies(String query) {  // charge les film correspondant a la recherche et present dans firebase du user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("movies");
            Query searchQuery = userMoviesRef.orderByChild("title").startAt(query).endAt(query + "\uf8ff");

            searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    movieList.clear();
                    for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                        Movie movie = movieSnapshot.getValue(Movie.class);
                        if (movie != null) {
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

    //Méthode appelée lorsqu'un élément de la RecyclerView est cliqué
    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(Liste_personnelle.this, FilmDetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    //Méthode pour masquer le clavier virtuel
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
        }
    }
}
