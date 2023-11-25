package com.example.watch_tracker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Season implements Parcelable {
    @SerializedName("season_number")
    private int seasonNumber;

    @SerializedName("episode_count")
    private int episodeCount;

    @SerializedName("episodes")
    private List<Episode> episodes;

    // Ajoutez les autres propriétés nécessaires

    // Les méthodes getters et setters nécessaires

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public Season() {
        // Constructeur vide nécessaire pour Parcelable
    }

    protected Season(Parcel in) {
        seasonNumber = in.readInt();
        episodeCount = in.readInt();
        // Ajoutez la logique pour lire la liste d'épisodes depuis le Parcel
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(seasonNumber);
        dest.writeInt(episodeCount);
        // Ajoutez la logique pour écrire la liste d'épisodes dans le Parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Le reste de votre implémentation Parcelable
}