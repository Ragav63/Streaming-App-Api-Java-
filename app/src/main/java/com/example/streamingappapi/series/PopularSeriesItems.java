package com.example.streamingappapi.series;

import android.os.Parcel;
import android.os.Parcelable;

public class PopularSeriesItems implements Parcelable {
    String popularSeriesRating, popularSeriesTitle;
    int popularSeriesImg;

    public PopularSeriesItems(String popularSeriesRating, String popularSeriesTitle, int popularSeriesImg) {
        this.popularSeriesRating = popularSeriesRating;
        this.popularSeriesImg = popularSeriesImg;
        this.popularSeriesTitle =popularSeriesTitle;
    }

    protected PopularSeriesItems(Parcel in) {
        popularSeriesRating = in.readString();
        popularSeriesTitle = in.readString();
        popularSeriesImg = in.readInt();
    }

    public static final Creator<PopularSeriesItems> CREATOR = new Creator<PopularSeriesItems>() {
        @Override
        public PopularSeriesItems createFromParcel(Parcel in) {
            return new PopularSeriesItems(in);
        }

        @Override
        public PopularSeriesItems[] newArray(int size) {
            return new PopularSeriesItems[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(popularSeriesRating);
        dest.writeString(popularSeriesTitle);
        dest.writeInt(popularSeriesImg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and setters
    public String getPopularSeriesRating() {
        return popularSeriesRating;
    }

    public void setPopularSeriesRating(String popularSeriesRating) {
        this.popularSeriesRating = popularSeriesRating;
    }

    public String getPopularSeriesTitle() {
        return popularSeriesTitle;
    }

    public void setPopularSeriesTitle(String popularSeriesTitle) {
        this.popularSeriesTitle = popularSeriesTitle;
    }

    public int getPopularSeriesImg() {
        return popularSeriesImg;
    }

    public void setPopularSeriesImg(int popularSeriesImg) {
        this.popularSeriesImg = popularSeriesImg;
    }

    @Override
    public String toString() {
        return "PopularSeriesItems{" +
                "popularSeriesRating='" + popularSeriesRating + '\'' +
                ", popularSeriesTitle=" +popularSeriesTitle +
                ", popularSeriesImg=" + popularSeriesImg +
                '}';
    }
}
