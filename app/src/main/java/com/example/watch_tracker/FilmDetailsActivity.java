package com.example.watch_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.example.watch_tracker.Movie;
import android.widget.ImageView;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;

public class FilmDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);

        ImageView mask = findViewById(R.id.retourButton);
        ImageView mask2 = findViewById(R.id.addButton);
        ImageView mask3 = findViewById(R.id.shareButton);
        ImageView mask4 = findViewById(R.id.deleteButton);

        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // Utilisateur authentifié
                    String userId = currentUser.getUid();

                    // Créez une référence à la base de données pour l'utilisateur actuel
                    DatabaseReference userMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("movies");

                    // Récupérez les informations du film
                    Movie movie = getIntent().getParcelableExtra("movie");

                    // Vérifiez si le film n'est pas déjà enregistré par l'utilisateur
                    userMoviesRef.child(String.valueOf(movie.getId())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Firebase", "onDataChange: dataSnapshot.exists() = " + dataSnapshot.exists());

                            if (!dataSnapshot.exists()) {
                                // Le film n'est pas encore enregistré, ajoutez-le à la base de données
                                userMoviesRef.child(String.valueOf(movie.getId())).setValue(movie);
                                Toast.makeText(FilmDetailsActivity.this, "Film enregistré avec succès", Toast.LENGTH_SHORT).show();
                            } else {
                                // Le film est déjà enregistré
                                Toast.makeText(FilmDetailsActivity.this, "Ce film est déjà enregistré", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Gestion des erreurs de base de données
                            Log.e("Firebase", "Erreur lors de l'accès à la base de données : " + databaseError.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(FilmDetailsActivity.this, "Vous etes pas authentifie", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mask3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FilmDetailsActivity.this, "en cours de Developpement", Toast.LENGTH_SHORT).show();
            }
        });

        mask4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {

                    String userId = currentUser.getUid();


                    Movie movie = getIntent().getParcelableExtra("movie");

                    if (movie != null) {

                        DatabaseReference userMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("movies").child(String.valueOf(movie.getId()));


                        userMoviesRef.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {

                                    Toast.makeText(FilmDetailsActivity.this, "Film supprimé avec succès", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {

                                    Log.e("Firebase", "Erreur lors de la suppression du film : " + databaseError.getMessage());
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(FilmDetailsActivity.this, "Vous n'êtes pas authentifié", Toast.LENGTH_SHORT).show();
                }
            }
        });




        // Récupérez les informations du film
        Movie movie = getIntent().getParcelableExtra("movie");

        // Affichez les informations
        if (movie != null) {
            TextView titleTextView = findViewById(R.id.titleTextView);
            titleTextView.setText(movie.getTitle());

            // Chargez l'image du film avec Picasso
            ImageView moviePoster = findViewById(R.id.affiche);
            Picasso.get()
                    .load(movie.getPosterPath())
                    .error(R.drawable.placeholder_image)
                    .into(moviePoster, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Image chargée avec succès
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Picasso", "Error loading image: " + e.getMessage());
                        }
                    });
            // Description du film
            TextView descriptionTextView = findViewById(R.id.movie_description);
            descriptionTextView.setText(movie.getOverview());
        }
    }
}
