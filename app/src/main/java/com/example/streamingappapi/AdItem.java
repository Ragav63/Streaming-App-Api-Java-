package com.example.streamingappapi;

public class AdItem {
    private int id;
    private String title;
    private String video;
    private double runtime;
    private String website;

    public AdItem(int id, String title, String video, double runtime, String website) {
        this.id = id;
        this.title = title;
        this.video = video;
        this.runtime = runtime;
        this.website = website;
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public double getRuntime() {
        return runtime;
    }

    public void setRuntime(double runtime) {
        this.runtime = runtime;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
