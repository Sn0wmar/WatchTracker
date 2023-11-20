package com.example.watch_tracker;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.example.watch_tracker.Movie;
import android.widget.ImageView;
import android.util.Log;

public class FilmDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);

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
        }
    }
}
