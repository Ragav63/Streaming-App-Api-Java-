package com.example.streamingappapi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CastItems implements Parcelable {
    private String name;
    private String designation;
    private List<String> images;
    private String about;

    public CastItems(String name, String designation, List<String> images, String about) {
        this.name = name;
        this.designation = designation;
        this.images = images;
        this.about = about;
    }

    protected CastItems(Parcel in) {
        name = in.readString();
        designation = in.readString();
        images = in.createStringArrayList();
        about = in.readString();
    }

    public static final Creator<CastItems> CREATOR = new Creator<CastItems>() {
        @Override
        public CastItems createFromParcel(Parcel in) {
            return new CastItems(in);
        }

        @Override
        public CastItems[] newArray(int size) {
            return new CastItems[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(designation);
        dest.writeStringList(images);
        dest.writeString(about);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
