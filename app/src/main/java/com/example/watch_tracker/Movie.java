package com.example.watch_tracker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";

    // Constructeur sans argument nécessaire pour Firebase
    public Movie() {
        // Constructeur vide requis par Firebase Realtime Database.
    }

    // Constructeur avec arguments
    public Movie(int id, String title, String overview, String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    // Getters
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


    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
    }

}
