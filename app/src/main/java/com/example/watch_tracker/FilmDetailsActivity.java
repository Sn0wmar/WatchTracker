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

public class FilmDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);

        ImageView mask = findViewById(R.id.retourButton);
        ImageView mask2 = findViewById(R.id.addButton);
        ImageView mask3 = findViewById(R.id.shareButton);

        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), Bouton.class);
                startActivity(it);
            }
        });

        mask2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FilmDetailsActivity.this, "en cours de Developpement", Toast.LENGTH_SHORT).show();
            }
        });

        mask3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FilmDetailsActivity.this, "en cours de Developpement", Toast.LENGTH_SHORT).show();
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
