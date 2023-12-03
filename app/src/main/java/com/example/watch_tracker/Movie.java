package com.example.watch_tracker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

//Déclaration de la classe Movie qui implémente l'interface Parcelable
public class Movie implements Parcelable {

    //Déclaration des variables
    private int id;

    @SerializedName(value = "title", alternate = {"name"})
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("statut")
    private String statut;

    @SerializedName("Fav")
    private String Fav;

    @SerializedName("seasons")
    private List<Season> seasons;

    //URL de base pour les images des films
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";



    //Cosntructeur par défaut
    public Movie() {

    }

    //Constructeur avec paramètres
    public Movie(int id, String title, String overview, String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    //Méthodes setter pour statut et Fav
    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setFav(String Fav){this.Fav = Fav;}

    //Méthode getter pour id, tittle, overview, posterPath, statut, Fav et seasons
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return BASE_IMAGE_URL + posterPath;
    }

    public String getStatut() {return statut;}

    public String getFav(){return Fav;}



    public List<Season> getSeasons() {
        return seasons;
    }

    //Méthode setter pour seasons
    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    //Constructeur prenant un Parcel pour la création d'un objet Movie
    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
    }

    //Créateur Parcelable pour l'objet Movie
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    //Méthode pour décrire le contenu de l'objet Parcelable
    @Override
    public int describeContents() {
        return 0;
    }


    //Méthode pour écrire les valeurs de l'objet dans un Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
    }

}
