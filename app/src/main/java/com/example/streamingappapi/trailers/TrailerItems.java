package com.example.streamingappapi.trailers;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TrailerItems implements Parcelable {
    private String trailerTitle;
    private List<String> trailerTimings;
    private List<String> trailerUrls;

    public TrailerItems(String trailerTitle, List<String> trailerTimings, List<String> trailerUrls) {
        this.trailerTitle = trailerTitle;
        this.trailerTimings = trailerTimings;
        this.trailerUrls = trailerUrls;
    }

    protected TrailerItems(Parcel in) {
        trailerTitle = in.readString();
        trailerTimings = in.createStringArrayList();
        trailerUrls = in.createStringArrayList();
    }

    public static final Creator<TrailerItems> CREATOR = new Creator<TrailerItems>() {
        @Override
        public TrailerItems createFromParcel(Parcel in) {
            return new TrailerItems(in);
        }

        @Override
        public TrailerItems[] newArray(int size) {
            return new TrailerItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerTitle);
        dest.writeStringList(trailerTimings);
        dest.writeStringList(trailerUrls);
    }

    public String getTrailerTitle() {
        return trailerTitle;
    }

    public void setTrailerTitle(String trailerTitle) {
        this.trailerTitle = trailerTitle;
    }

    public List<String> getTrailerTimings() {
        return trailerTimings;
    }

    public void setTrailerTimings(List<String> trailerTimings) {
        this.trailerTimings = trailerTimings;
    }

    public List<String> getTrailerUrls() {
        return trailerUrls;
    }

    public void setTrailerUrls(List<String> trailerUrls) {
        this.trailerUrls = trailerUrls;
    }
}
