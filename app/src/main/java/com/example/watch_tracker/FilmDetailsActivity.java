package com.example.watch_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.example.watch_tracker.Movie;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.watch_tracker.DescriptionAdapter;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class FilmDetailsActivity extends AppCompatActivity {



    private TextView titleTextView;
    private RecyclerView rvDescription;
    private DescriptionAdapter descriptionAdapter;
    private List<String> descriptionList;


    private RecyclerView rvEpisodes;
    private Spinner spinnerSeason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);



        //Initialisation des composants d'interface utilisateur
        titleTextView = findViewById(R.id.titleTextView);
        rvDescription = findViewById(R.id.rv_description);
        rvEpisodes = findViewById(R.id.rvEpisodes);
        spinnerSeason = findViewById(R.id.spinnerSeason);

        //Initialisation de l'adaptateur pour la liste de descriptions
        descriptionAdapter = new DescriptionAdapter(descriptionList);
        rvDescription.setAdapter(descriptionAdapter);
        rvDescription.setLayoutManager(new LinearLayoutManager(this));




        //Détection d'appui sur les boutons de l'interface
        ImageView mask = findViewById(R.id.retourButton);
        ImageView mask2 = findViewById(R.id.addButton);
        ImageView mask3 = findViewById(R.id.shareButton);
        ImageView mask4 = findViewById(R.id.deleteButton);
        ImageView mask5 = findViewById(R.id.star);
        ImageView mask6 = findViewById(R.id.pasvu);
        ImageView mask7 = findViewById(R.id.encours);
        ImageView mask8 = findViewById(R.id.vu);

        //Retour à l'activité précèdente
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            } // ferme activity
        });

        //Ajout du film dans la liste de l'user
        mask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Récupère info user
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    //Ajout film dans Firebase
                    DatabaseReference userMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("movies");
                    Movie movie = getIntent().getParcelableExtra("movie");

                    //Ajout d'un statut par défaut
                    movie.setStatut("Pas vu");
                    //Ajout dans la liste des non favoris
                    movie.setFav("non");

                    userMoviesRef.child(String.valueOf(movie.getId())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Firebase", "onDataChange: dataSnapshot.exists() = " + dataSnapshot.exists());

                            if (!dataSnapshot.exists()) {
                                //Le film n'est pas encore enregistré, on l'ajoute à la base de données
                                userMoviesRef.child(String.valueOf(movie.getId())).setValue(movie);
                                Toast.makeText(FilmDetailsActivity.this, "Contenu enregistré avec succès", Toast.LENGTH_SHORT).show();
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

                if (movie != null) { //Partage des informations du film
                    String title = movie.getTitle();
                    String description = movie.getOverview();

                    String shareText = title + "\n" + description;

                    //Création de l'intent de partage
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


        //Suppression du fil à la liste de l'user
        mask4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {

                    String userId = currentUser.getUid();


                    Movie movie = getIntent().getParcelableExtra("movie");

                    if (movie != null) {//Suppression du film de Firebase


                        DatabaseReference userMoviesRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("movies").child(String.valueOf(movie.getId()));


                        userMoviesRef.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {

                                    Toast.makeText(FilmDetailsActivity.this, "Contenu supprimé avec succès", Toast.LENGTH_SHORT).show();
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

        //Ajout/Retrait du film des favoris
        mask5.setOnClickListener(new View.OnClickListener() {
            //Modifie la valeur de l'attribut Fav
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

                        //Récupère la valeur actuelle de Fav
                        userMoviesRef.child("fav").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String ancienneValeur = dataSnapshot.getValue(String.class);

                                    //Bascule entre "oui" et "non"
                                    String nouvelleValeur = (ancienneValeur.equals("oui")) ? "non" : "oui";

                                    //Mettre à jour la valeur dans la base de données
                                    userMoviesRef.child("fav").setValue(nouvelleValeur);


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


        //Gestion du changement de statut "Pas vu" du film
        mask6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    //Récupère les informations du film
                    Movie movie = getIntent().getParcelableExtra("movie");

                    if (movie != null) {
                        DatabaseReference userMoviesRef = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("users")
                                .child(userId)
                                .child("movies")
                                .child(String.valueOf(movie.getId()));

                        //Mis à jour du statut directement dans la base de données
                        userMoviesRef.child("statut").setValue("Pas vu")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //La mise à jour du statut a réussi
                                        Toast.makeText(FilmDetailsActivity.this, "Statut mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //La mise à jour du statut a échoué
                                        Toast.makeText(FilmDetailsActivity.this, "Échec de la mise à jour du statut : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(FilmDetailsActivity.this, "Vous n'êtes pas authentifié", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Gestion du chargement de statut "En cours" du film
        mask7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    //Récupération des informations du film
                    Movie movie = getIntent().getParcelableExtra("movie");

                    if (movie != null) {
                        DatabaseReference userMoviesRef = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("users")
                                .child(userId)
                                .child("movies")
                                .child(String.valueOf(movie.getId()));

                        //Mis à jour le statut directement dans la base de donées
                        userMoviesRef.child("statut").setValue("En cours")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //La mise à jour du statut a réussi
                                        Toast.makeText(FilmDetailsActivity.this, "Statut mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //La mise à jour du statut a échoué
                                        Toast.makeText(FilmDetailsActivity.this, "Échec de la mise à jour du statut : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(FilmDetailsActivity.this, "Vous n'êtes pas authentifié", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Gestion du changement de statut "Vu" du film
        mask8.setOnClickListener(new View.OnClickListener() {
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

                        //Mis à jour le statut directement
                        userMoviesRef.child("statut").setValue("Vu")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //La mise à jour du statut a réussi
                                        Toast.makeText(FilmDetailsActivity.this, "Statut mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //La mise à jour du statut a échoué
                                        Toast.makeText(FilmDetailsActivity.this, "Échec de la mise à jour du statut : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    Toast.makeText(FilmDetailsActivity.this, "Vous n'êtes pas authentifié", Toast.LENGTH_SHORT).show();
                }
            }
        });

        descriptionList = new ArrayList<>();   //Initialisation de la liste de description


        //Récupère les informations du film
        Movie movie = getIntent().getParcelableExtra("movie");


        //Affichage des informations du film
        if (movie != null) {
            titleTextView.setText(movie.getTitle());

            //Chargement de l'affiche du film
            ImageView moviePoster = findViewById(R.id.affiche);
            Picasso.get()
                    .load(movie.getPosterPath())
                    .error(R.drawable.placeholder_image)
                    .into(moviePoster, new Callback() {
                        @Override
                        public void onSuccess() {
                            //Affiche chargée avec succès
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Picasso", "Error loading image: " + e.getMessage());
                        }
                    });




            //Initialise la liste de description
            descriptionList = new ArrayList<>();



                if (movie.getSeasons() != null && !movie.getSeasons().isEmpty()) {
                    //Affiche les saisons et les épisodes
                    List<String> seasonDescriptions = new ArrayList<>();
                    for (Season season : movie.getSeasons()) {
                        //Ajoute les descriptions des saisons à la liste
                        seasonDescriptions.add("Season " + season.getSeasonNumber());
                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, seasonDescriptions);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSeason.setAdapter(spinnerAdapter);

                    //Initialise le RecyclerView des épisodes
                    List<Episode> episodes = movie.getSeasons().get(0).getEpisodes();
                    EpisodeAdapter episodeAdapter = new EpisodeAdapter(this, episodes);
                    rvEpisodes.setLayoutManager(new LinearLayoutManager(this));
                    rvEpisodes.setAdapter(episodeAdapter);
                } else {
                    //Affiche la description du film
                    descriptionList.add(movie.getOverview());
                    DescriptionAdapter descriptionAdapter = new DescriptionAdapter(descriptionList);
                    rvDescription.setLayoutManager(new LinearLayoutManager(this));
                    rvDescription.setAdapter(descriptionAdapter);
                }



        }
    }
}