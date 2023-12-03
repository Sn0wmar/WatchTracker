package com.example.watch_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.watch_tracker.Episode;





import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    //Liste des épisodes à afficher
    private List<Episode> episodes;

    //Contexte de l'application
    private Context context;

    //Constructeur de l'adaptateur
    public EpisodeAdapter(Context context, List<Episode> episodes) {
        this.context = context;
        this.episodes = episodes;
    }

    //Méthode appelée lors de la création d'un ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_episode, parent, false);
        return new ViewHolder(view);
    }

    //Méthode appelée lors de la liaison des données à un ViewHolder spécifique
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Episode episode = episodes.get(position);

        //MAJ les vues du ViewHolder avec les données de l'épisode
        holder.titleTextView.setText(episode.getTitle());
        holder.descriptionTextView.setText(episode.getOverview());
    }


    //Méthode retournant le nombre d'éléments dans la liste d'épisodes
    @Override
    public int getItemCount() {
        return episodes.size();
    }

    //Classe interne représentant le ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        //Composants d'interface utilisateur à afficher dans chaque élément de la liste
        TextView titleTextView;
        TextView descriptionTextView;

        //Constructeur du ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialisation des TextView avec les élements du layout
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
