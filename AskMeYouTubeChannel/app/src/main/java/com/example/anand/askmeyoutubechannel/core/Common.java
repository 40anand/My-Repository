package com.example.anand.askmeyoutubechannel.core;

public class Common {

    public static final String API_KEY = "AIzaSyB1AljpOL_urnXV5hFD4mIeWBWsnfsOrBU";
    public static final String CHANNEL_ID = "UCkqpGeRn9P6-W2vK2qopo0w";
    public final static String API_LINK =
            "https://www.googleapis.com/youtube/v3/search?key=" + API_KEY
                    + "&channelId=" + CHANNEL_ID
                    + "&part=snippet,id&order=date&maxResults=20";

    public static final String YOUTUBE_VIDEO_VIEW_URL = "https://www.youtube.com/watch?v=";

    public static final String VIDEO_ID = "video_id";
    public static final String TITLE = "title";

    public static String getUrlForNextPage(String nextPageToken) {
        return API_LINK + "&pageToken=" + nextPageToken;
    }
}
