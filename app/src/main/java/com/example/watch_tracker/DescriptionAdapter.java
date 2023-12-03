package com.example.watch_tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;

public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.ViewHolder> {

    //Liste des descriptions à afficher
    private List<String> descriptionList;

    //Liste des saisons initialisée vide par défaut
    protected List<Season> seasons;

    //Constructeur de l'adaptateur
    public DescriptionAdapter(List<String> descriptionList) { // affiche la description du contenu
        this.descriptionList = descriptionList;
        this.seasons = new ArrayList<>();
    }

    //Métgode pour définir la liste des descriptions
    public void setDescriptionList(List<String> descriptionList) {
        this.descriptionList = descriptionList;
        notifyDataSetChanged();
    }

    //Méthode pour définir la liste des saisons
    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
        notifyDataSetChanged();
    }

    //Classe statique interne pour gérer l'adaptateur des description de saisons
  public static class SeasonDescriptionAdapter extends RecyclerView.Adapter<ViewHolder> {

       //Liste des saisons à afficher
      private List<Season> seasons;

      //Constructeur de l'adaptateur des descriptions de saisons
      public SeasonDescriptionAdapter(List<Season> seasons) {
          this.seasons = seasons;
      }

      //Création d'un nouvel élément de vue
      @Override
      public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.description_item, parent, false);
          return new ViewHolder(view);
      }

      //Liaison des données à l'élément de vue
      @Override
      public void onBindViewHolder(ViewHolder holder, int position) {
          Season season = seasons.get(position);
          holder.descriptionTextView.setText(season.getSeasonName());
      }

      //Retourne le nombre total d'éléments dans la liste
      @Override
      public int getItemCount() {
          return seasons.size();
      }
  }

  //Classe statique interne pour représenter un élement de vue
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionTextView;

        //Constructeur de la classe ViewHolder
        public ViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }

    //Création d'un nouvel élément de vue
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.description_item, parent, false);
        return new ViewHolder(view);
    }

    //Lie les données à l'élément de vue
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String description = descriptionList.get(position);
        holder.descriptionTextView.setText(description);
    }

    //Retourne le nombre total d'éléments dans la liste
    @Override
    public int getItemCount() {
        return descriptionList.size();
    }
}

