package com.example.streamingappapi.movie;

import android.os.Parcel;
import android.os.Parcelable;

public class PopularMovieItems implements Parcelable {
    String popularMovieRating, popularMovieTitle;
    int popularMovieImg;

    public PopularMovieItems(String popularMovieRating, String popularMovieTitle, int popularMovieImg) {
        this.popularMovieRating = popularMovieRating;
        this.popularMovieImg = popularMovieImg;
        this.popularMovieTitle = popularMovieTitle;
    }

    protected PopularMovieItems(Parcel in) {
        popularMovieRating = in.readString();
        popularMovieTitle = in.readString();
        popularMovieImg = in.readInt();
    }

    public static final Creator<PopularMovieItems> CREATOR = new Creator<PopularMovieItems>() {
        @Override
        public PopularMovieItems createFromParcel(Parcel in) {
            return new PopularMovieItems(in);
        }

        @Override
        public PopularMovieItems[] newArray(int size) {
            return new PopularMovieItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(popularMovieRating);
        dest.writeString(popularMovieTitle);
        dest.writeInt(popularMovieImg);
    }
    public String getPopularMovieRating() {
        return popularMovieRating;
    }

    public void setPopularMovieRating(String popularMovieRating) {
        this.popularMovieRating = popularMovieRating;
    }

    public String getPopularMovieTitle() {
        return popularMovieTitle;
    }

    public void setPopularMovieTitle(String popularMovieTitle) {
        this.popularMovieTitle = popularMovieTitle;
    }

    public int getPopularMovieImg() {
        return popularMovieImg;
    }

    public void setPopularMovieImg(int popularMovieImg) {
        this.popularMovieImg = popularMovieImg;
    }
}
