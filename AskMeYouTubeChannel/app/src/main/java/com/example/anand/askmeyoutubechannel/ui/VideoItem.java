package com.example.anand.askmeyoutubechannel.ui;


public class VideoItem {
    private String videoId;
    private String title;
    private String thumbUrl;

    public VideoItem() {
        videoId = null;
        title = null;
        thumbUrl = null;
    }


    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
