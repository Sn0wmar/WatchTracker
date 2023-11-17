package com.example.watch_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private List<Movie> movies;
    private LayoutInflater inflater;

    // Constructeur
    public RVAdapter(Context context, List<Movie> movies) {
        this.inflater = LayoutInflater.from(context);
        this.movies = movies;
    }

    // Créer une nouvelle vue (appelée par le layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the individual item layout here
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    // Remplace le contenu de la vue (appelé par le layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.titleTextView.setText(movie.getTitle());
    }

    // Retourne la taille de votre ensemble de données (appelé par le layout manager)
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // Fournit une référence aux éléments de vue à l'intérieur d'une vue de données
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_movie_title);
        }
    }
}
