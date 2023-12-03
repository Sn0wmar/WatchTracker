package com.example.watch_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.watch_tracker.Season;
import java.util.List;

//Adaptateur pour afficher une liste de saisons dans un RecyclerView
public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder> {

    private List<Season> seasons;
    private Context context;


    //Constructeur de l'adaptateur avec le contexte et la liste des saisons
    public SeasonAdapter(Context context, List<Season> seasons) {
        this.context = context;
        this.seasons = seasons;
    }

    //Méthode appelée lors de la création d'une nouvelle vue pour un élément de la liste
    @NonNull
    @Override
    public SeasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        return new SeasonViewHolder(view);
    }

    //Méthode appelée pour afficher les données d'un élément à une position donnée
    @Override
    public void onBindViewHolder(@NonNull SeasonViewHolder holder, int position) {
        holder.textView.setText(seasons.get(position).getSeasonName());
    }

    //Méthode retournant le nombre total d'éléments dans la liste
    @Override
    public int getItemCount() {
        return seasons.size();
    }

    //Classe interne représentant le ViewHolder pour chaque élément de la liste
    public static class SeasonViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        //Constructeur qui initialise les vues avec les éléments du layout
        public SeasonViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
