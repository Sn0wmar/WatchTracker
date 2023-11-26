package com.example.watch_tracker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Movie implements Parcelable {
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

    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";




    public Movie() {

    }


    public Movie(int id, String title, String overview, String posterPath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setFav(String Fav){this.Fav = Fav;}

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

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
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
