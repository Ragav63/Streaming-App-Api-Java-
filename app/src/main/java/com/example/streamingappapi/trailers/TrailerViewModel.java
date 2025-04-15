package com.example.streamingappapi.trailers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class TrailerViewModel extends ViewModel {
    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<String> runtime = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> trailers = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<TrailerItems>> trailerItems = new MutableLiveData<>();
    private final MutableLiveData<String> url = new MutableLiveData<>();
    private final MutableLiveData<String> videoId = new MutableLiveData<>();
    private final MutableLiveData<Float> currentTime = new MutableLiveData<>();



    public LiveData<String> getTitle() { return title; }
    public void setTitle(String value) { title.setValue(value); }

    public LiveData<String> getRuntime() { return runtime; }
    public void setRuntime(String value) { runtime.setValue(value); }


    public LiveData<ArrayList<String>> getTrailers() { return trailers; }
    public void setTrailers(ArrayList<String> value) { trailers.setValue(value); }

    public LiveData<ArrayList<TrailerItems>> getTrailerItems() { return trailerItems; }
    public void setTrailerItems(ArrayList<TrailerItems> value) { trailerItems.setValue(value); }

    public LiveData<String> getUrl() { return url; }
    public void setUrl(String value) { url.setValue(value); }

    public LiveData<String> getVideoId() { return videoId; }
    public void setVideoId(String value) { videoId.setValue(value); }

    public LiveData<Float> getCurrentTime() { return currentTime; }
    public void setCurrentTime(Float value) { currentTime.setValue(value); }
}
