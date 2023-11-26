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


    @SerializedName("season_name")
    private String seasonName;


    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }


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

    }


    protected Season(Parcel in) {
        seasonNumber = in.readInt();
        episodeCount = in.readInt();
        episodes = in.createTypedArrayList(Episode.CREATOR);
        seasonName = in.readString();

    }

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
