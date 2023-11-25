package com.example.watch_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;                      //
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.example.watch_tracker.Movie;
import android.widget.ImageView;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;                                            //




public class FilmDetailsActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView movieDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);

        titleTextView = findViewById(R.id.titleTextView);
        movieDescription = findViewById(R.id.movie_description);


        ImageView mask = findViewById(R.id.retourButton);
        ImageView mask2 = findViewById(R.id.addButton);
        ImageView mask3 = findViewById(R.id.shareButton);
        ImageView mask4 = findViewById(R.id.deleteButton);
        ImageView mask5 = findViewById(R.id.star);

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
                    String userId = currentUser.getUid();
                    DatabaseReference userMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("movies");

                    Movie movie = getIntent().getParcelableExtra("movie");


                    movie.setStatut("Pas vu");

                    movie.setFav("non");

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
                            Log.e("Firebase", "Erreur lors de l'accès à la base de données : " + databaseError.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(FilmDetailsActivity.this, "Vous n'êtes pas authentifié", Toast.LENGTH_SHORT).show();
                }
            }

        });

        mask3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie movie = getIntent().getParcelableExtra("movie");

                if (movie != null) {
                    String title = movie.getTitle();
                    String description = movie.getOverview();

                    String shareText = title + "\n" + description;

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                } else {
                    Toast.makeText(FilmDetailsActivity.this, "Aucun film disponible pour le partage", Toast.LENGTH_SHORT).show();
                }
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

        mask5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    Movie movie = getIntent().getParcelableExtra("movie");

                    if (movie != null) {
                        DatabaseReference userMoviesRef = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("users")
                                .child(userId)
                                .child("movies")
                                .child(String.valueOf(movie.getId()));

                        // Récupérez la valeur actuelle de Fav
                        userMoviesRef.child("fav").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String ancienneValeur = dataSnapshot.getValue(String.class);

                                    // Basculez entre "oui" et "non"
                                    String nouvelleValeur = (ancienneValeur.equals("oui")) ? "non" : "oui";

                                    // Mettez à jour la valeur dans la base de données
                                    userMoviesRef.child("fav").setValue(nouvelleValeur);

                                    // Affichez le message approprié
                                    if (nouvelleValeur.equals("oui")) {
                                        Toast.makeText(FilmDetailsActivity.this, "Ajouté aux favoris", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(FilmDetailsActivity.this, "Retiré des favoris", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("Firebase", "Erreur lors de l'accès à la base de données : " + databaseError.getMessage());
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