package com.example.watch_tracker;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.watch_tracker.Movie;

public class FilmDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_details);

        // Récupérez les informations du film de l'intent
        Movie movie = getIntent().getParcelableExtra("movie");

        // Affichez les informations dans votre mise en page
        if (movie != null) {
            TextView titleTextView = findViewById(R.id.titleTextView);
            titleTextView.setText(movie.getTitle());

            // Ajoutez d'autres vues pour afficher d'autres détails du film
            // Exemple : TextView overviewTextView = findViewById(R.id.overviewTextView);
            // overviewTextView.setText(movie.getOverview());
        }
    }
}
