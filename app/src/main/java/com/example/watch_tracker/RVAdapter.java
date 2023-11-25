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

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public RVAdapter(Context context, List<Movie> movies, OnItemClickListener onItemClickListener) {
        this.movies = movies;
        this.onItemClickListener = onItemClickListener;


    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_poster);
            movieTitle = itemView.findViewById(R.id.movie_title);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        Movie movie = movies.get(position);
        holder.movieTitle.setText(movie.getTitle());
        // Chargez l'image du film avec Picasso

        Picasso.get()
                .load(movie.getPosterPath())
                .error(R.drawable.placeholder_image)
                .into(holder.moviePoster, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Image chargée avec succès
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Picasso", "Error loading image: " + e.getMessage());
                        Log.e("Picasso", "Failed URL: " + movie.getPosterPath());
                    }
                });




        // clics sur un élément
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Appel de la méthode onItemClick de l'interface
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(movies.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


}
