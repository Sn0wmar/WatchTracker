package com.example.watch_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.watch_tracker.Movie;
import com.squareup.picasso.Picasso;
import java.util.List;
import android.util.Log;
import com.squareup.picasso.Callback;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnItemClickListener onItemClickListener;


    //Interface pour gérer les clis sur les éléments de la liste
    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    //Constructeur de l'adaptateur avec la liste de films et l'interface pour les clics
    public RVAdapter(Context context, List<Movie> movies, OnItemClickListener onItemClickListener) {
        this.movies = movies;
        this.onItemClickListener = onItemClickListener;


    }

    //Classe interne pour contenir les vues des éléments de la liste
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle;

        //Constructeur qui initialise les vues avec les éléments du layout
        public MovieViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_poster);
            movieTitle = itemView.findViewById(R.id.movie_title);
        }
    }

    //Méthode appelée lors de la création d'une nouvelle vue pour un élément de liste
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(v);
    }

    //Méthode appelée pour afficher les données d'un élément à une position donnée
    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        Movie movie = movies.get(position);
        holder.movieTitle.setText(movie.getTitle());


        //Utilisation de Picasso pour charger l'image du film depuis son URL
        Picasso.get()
                .load(movie.getPosterPath())
                .error(R.drawable.placeholder_image)
                .into(holder.moviePoster, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Picasso", "Error loading image: " + e.getMessage());
                        Log.e("Picasso", "Failed URL: " + movie.getPosterPath());
                    }
                });




        //Gestion des clics sur un élément de la liste
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(movies.get(position));
                }
            }
        });
    }

    //Retourne le nombre totla d'éléments dans la liste
    @Override
    public int getItemCount() {
        return movies.size();
    }


}
