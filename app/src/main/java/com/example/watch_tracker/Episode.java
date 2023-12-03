package com.example.watch_tracker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Episode implements Parcelable {

    //Attributs de la classe Episode
    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    //Constructeur par défaut
    public Episode() {
    }


    //Constructeur utilisé lors de la reconstruction de l'objet depuis un Parcel
    protected Episode(Parcel in) {

        title = in.readString();
        overview = in.readString();

    }


    //Méthode pour écrire les valeurs de l'objet dans un Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);

    }


    //Méthode pour décrire les types spéciaux d'objets contenus dans le Parcel
    @Override
    public int describeContents() {
        return 0;
    }


    //Créateur statique utilisé pour créer une instance de la classe à partir d'un Parcel
    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };


    //Méthodes getter et setter pour accéder et modifier les attributs privés
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


}
