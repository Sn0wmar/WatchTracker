package com.example.watch_tracker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

//Classe représentant une saison d'une série
public class Season implements Parcelable {
    @SerializedName("season_number")
    private int seasonNumber;

    @SerializedName("episode_count")
    private int episodeCount;

    @SerializedName("episodes")
    private List<Episode> episodes;


    @SerializedName("season_name")
    private String seasonName;


    //Méthode getter pour définir le nom et la saison
    public String getSeasonName() {
        return seasonName;
    }

    //Méthode setter pour définir le nom de la saison
    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }


    //Méthode getter pour obtenir le numéro de la saison
    public int getSeasonNumber() {
        return seasonNumber;
    }

    //Méthode setter pour définir le numéro de la saison
    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    //Méthode getter pour obtenir le nombre d'épisodes de la saison
    public int getEpisodeCount() {
        return episodeCount;
    }


    //Méthode setter pour obtenir le nombre d'épisodes de la saison
    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }

    //Méthode getter pour obtenur la liste des épisodes de la saison
    public List<Episode> getEpisodes() {
        return episodes;
    }



    //Méthode setter pour définir la liste des épisodes de la saison
    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }


    //Constructeur par défaut
    public Season() {

    }


    //Constructeur utilisé pour recréer un objet Season à partir d'un Parcel
    protected Season(Parcel in) {
        seasonNumber = in.readInt();
        episodeCount = in.readInt();
        episodes = in.createTypedArrayList(Episode.CREATOR);
        seasonName = in.readString();

    }

    //Méthode pour écrire les valeurs de l'objet Season dans un Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(seasonNumber);
        dest.writeInt(episodeCount);
        dest.writeTypedList(episodes);
        dest.writeString(seasonName);  //

    }


    @Override
    public int describeContents() {
        return 0;
    }


}
