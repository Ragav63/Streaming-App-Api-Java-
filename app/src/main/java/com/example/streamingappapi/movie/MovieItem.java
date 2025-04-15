package com.example.streamingappapi.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.streamingappapi.CastItems;

import java.util.List;

public class MovieItem implements Parcelable{
    private int id;
    private String title;
    private String poster;
    private int year;
    private String country;
    private double imdb_rating;
    private List<String> genres;
    private List<CastItems> crew;
    private String plot;
    private List<String> trailers;
    private int runtime;
    private String awards;
    private String language;
    private String boxOffice;
    private String production;
    private String website;
    private List<String> images;
    private String url;

    public MovieItem(int id, String title, String poster, int year, String country, double imdb_rating, List<String> genres, List<CastItems> crew, String plot, List<String> trailers, int runtime, String awards, String language, String boxOffice, String production, String website, List<String> images, String url) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.year = year;
        this.country = country;
        this.imdb_rating = imdb_rating;
        this.genres = genres;
        this.crew = crew;
        this.plot = plot;
        this.trailers = trailers;
        this.runtime = runtime;
        this.awards = awards;
        this.language = language;
        this.boxOffice = boxOffice;
        this.production = production;
        this.website = website;
        this.images = images;
        this.url = url;
    }

    protected MovieItem(Parcel in) {
        id = in.readInt();
        title = in.readString();
        poster = in.readString();
        year = in.readInt();
        country = in.readString();
        imdb_rating = in.readDouble();
        genres = in.createStringArrayList();
        crew = in.createTypedArrayList(CastItems.CREATOR);
        plot = in.readString();
        trailers = in.createStringArrayList();
        runtime = in.readInt();
        awards = in.readString();
        language = in.readString();
        boxOffice = in.readString();
        production = in.readString();
        website = in.readString();
        images = in.createStringArrayList();
        url = in.readString();
    }


    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
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
        dest.writeString(poster);
        dest.writeInt(year);
        dest.writeString(country);
        dest.writeDouble(imdb_rating);
        dest.writeStringList(genres);
        dest.writeTypedList(crew);
        dest.writeString(plot);
        dest.writeStringList(trailers);
        dest.writeInt(runtime);
        dest.writeString(awards);
        dest.writeString(language);
        dest.writeString(boxOffice);
        dest.writeString(production);
        dest.writeString(website);
        dest.writeStringList(images);
        dest.writeString(url);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getImdb_rating() {
        return imdb_rating;
    }

    public void setImdb_rating(double imdb_rating) {
        this.imdb_rating = imdb_rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<CastItems> getCrew() {
        return crew;
    }

    public void setCrew(List<CastItems> crew) {
        this.crew = crew;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public List<String> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<String> trailers) {
        this.trailers = trailers;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
