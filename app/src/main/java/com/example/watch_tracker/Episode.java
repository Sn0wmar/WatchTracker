package com.example.watch_tracker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Episode implements Parcelable {
    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    // Ajoutez les autres propriétés nécessaires pour votre classe Episode

    // Constructeur vide nécessaire pour Parcelable
    public Episode() {
    }

    // Constructeur utilisé pour la désérialisation du Parcel
    protected Episode(Parcel in) {
        // Ajoutez la logique pour lire les propriétés depuis le Parcel
        title = in.readString();
        overview = in.readString();
        // Ajoutez la logique pour lire les autres propriétés
    }

    // Écrivez les propriétés dans le Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        // Ajoutez la logique pour écrire les autres propriétés
    }

    // Indique combien de types spéciaux sont présents dans l'objet Parcel
    @Override
    public int describeContents() {
        return 0;
    }

    // Permet de créer une instance de votre classe à partir d'un Parcel
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

    // Ajoutez les getters et setters nécessaires
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

    // Ajoutez les autres getters et setters nécessaires

    // Le reste de votre implémentation Parcelable
}
