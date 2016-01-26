package com.example.rabab.moviesfamily;

/**
 * Created by rabab on 18/12/15.
 */
public class VideoItem {
    private String videoId;
    private String key;
    private String name;


    public void setvideoId(String id) {
        this.videoId = id;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getvideoId() {
        return videoId;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
