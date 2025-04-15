package com.example.streamingappapi.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.streamingappapi.CastItems;

import java.util.ArrayList;

public class MovieViewModel extends ViewModel {
    private final MutableLiveData<String> imageResource = new MutableLiveData<>();
    private final MutableLiveData<String> rating = new MutableLiveData<>();
    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<String> plot = new MutableLiveData<>();
    private final MutableLiveData<String> year = new MutableLiveData<>();
    private final MutableLiveData<String> country = new MutableLiveData<>();
    private final MutableLiveData<String> runtime = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> genres = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> trailers = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<CastItems>> crew = new MutableLiveData<>();
    private final MutableLiveData<String> url = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> movieImages = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<MovieItem>> movieItemList = new MutableLiveData<>();
    private final MutableLiveData<String> videoId = new MutableLiveData<>();
    private final MutableLiveData<Float> currentTime = new MutableLiveData<>();

    public LiveData<String> getImageResource() { return imageResource; }
    public void setImageResource(String value) { imageResource.setValue(value); }

    public LiveData<String> getRating() { return rating; }
    public void setRating(String value) { rating.setValue(value); }

    public LiveData<String> getTitle() { return title; }
    public void setTitle(String value) { title.setValue(value); }

    public LiveData<String> getPlot() { return plot; }
    public void setPlot(String value) { plot.setValue(value); }

    public LiveData<String> getYear() { return year; }
    public void setYear(String value) { year.setValue(value); }

    public LiveData<String> getCountry() { return country; }
    public void setCountry(String value) { country.setValue(value); }

    public LiveData<String> getRuntime() { return runtime; }
    public void setRuntime(String value) { runtime.setValue(value); }

    public LiveData<ArrayList<String>> getGenres() { return genres; }
    public void setGenres(ArrayList<String> value) { genres.setValue(value); }

    public LiveData<ArrayList<String>> getTrailers() { return trailers; }
    public void setTrailers(ArrayList<String> value) { trailers.setValue(value); }

    public LiveData<ArrayList<CastItems>> getCrew() { return crew; }
    public void setCrew(ArrayList<CastItems> value) { crew.setValue(value); }

    public LiveData<String> getUrl() { return url; }
    public void setUrl(String value) { url.setValue(value); }

    public LiveData<ArrayList<String>> getMovieImages() { return movieImages; }
    public void setMovieImages(ArrayList<String> value) { movieImages.setValue(value); }

    public LiveData<ArrayList<MovieItem>> getMovieItemList() { return movieItemList; }
    public void setMovieItemList(ArrayList<MovieItem> value) { movieItemList.setValue(value); }

    public LiveData<String> getVideoId() { return videoId; }
    public void setVideoId(String value) { videoId.setValue(value); }

    public LiveData<Float> getCurrentTime() { return currentTime; }
    public void setCurrentTime(Float value) { currentTime.setValue(value); }
}
