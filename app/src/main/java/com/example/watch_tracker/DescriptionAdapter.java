package com.example.watch_tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;

public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.ViewHolder> {

    private List<String> descriptionList;
    protected List<Season> seasons;

    public DescriptionAdapter(List<String> descriptionList) { // affiche la description du contenu
        this.descriptionList = descriptionList;
        this.seasons = new ArrayList<>();
    }

    public void setDescriptionList(List<String> descriptionList) {
        this.descriptionList = descriptionList;
        notifyDataSetChanged();
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
        notifyDataSetChanged();
    }

  public static class SeasonDescriptionAdapter extends RecyclerView.Adapter<ViewHolder> {
      private List<Season> seasons;

      public SeasonDescriptionAdapter(List<Season> seasons) {
          this.seasons = seasons;
      }

      @Override
      public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.description_item, parent, false);
          return new ViewHolder(view);
      }

      @Override
      public void onBindViewHolder(ViewHolder holder, int position) {
          Season season = seasons.get(position);
          holder.descriptionTextView.setText(season.getSeasonName());
      }

      @Override
      public int getItemCount() {
          return seasons.size();
      }
  }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.description_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String description = descriptionList.get(position);
        holder.descriptionTextView.setText(description);
    }

    @Override
    public int getItemCount() {
        return descriptionList.size();
    }
}

