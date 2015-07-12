package com.example.anand.askmeyoutubechannel.core;

import android.util.Log;

import com.example.anand.askmeyoutubechannel.ui.VideoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoListParser {

    private static final String TAG = VideoListParser.class.getSimpleName();

    private static final String JSON_ARRAY_ITEMS = "items";
    private static final String JSON_OBJECT_SNIPPET = "snippet";
    private static final String JSON_OBJECT_ID = "id";
    private static final String JSON_OBJECT_THUMBNAILS = "thumbnails";
    private static final String JSON_OBJECT_THUMBNAILS_DEFAULT = "default";
    private static final String JSON_STRING_VIDEO_ID = "videoId";
    private static final String JSON_STRING_TITLE = "title";
    private static final String JSON_STRING_THUMBNAILS_DEFAULT_URL = "url";
    private static final String JSON_STRING_NEXT_PAGE_TOKEN = "nextPageToken";

    public static ArrayList<VideoItem> getVideoList(String jsonString) {

        ArrayList<VideoItem> videoItems = null;
        Log.d(TAG, jsonString);
        try {
            videoItems = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_ARRAY_ITEMS);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject currentJsonObject = jsonArray.getJSONObject(i);
                JSONObject currentSnippetObject = currentJsonObject.getJSONObject(JSON_OBJECT_SNIPPET);

                String videoId;
                try {
                    videoId = currentJsonObject.getJSONObject(JSON_OBJECT_ID).getString(JSON_STRING_VIDEO_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }
                String title = currentSnippetObject.getString(JSON_STRING_TITLE);
                String thumbUrl = currentSnippetObject.getJSONObject(JSON_OBJECT_THUMBNAILS).
                        getJSONObject(JSON_OBJECT_THUMBNAILS_DEFAULT).getString(JSON_STRING_THUMBNAILS_DEFAULT_URL);

                VideoItem videoItem = new VideoItem();
                videoItem.setVideoId(videoId);
                videoItem.setTitle(title);
                videoItem.setThumbUrl(thumbUrl);
                videoItems.add(videoItem);
            }
            return videoItems;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoItems;
    }

    public static ArrayList<VideoItem> loadNextPageVideoList(String jsonString, ArrayList<VideoItem> videoItems) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_ARRAY_ITEMS);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject currentJsonObject = jsonArray.getJSONObject(i);
                JSONObject currentSnippetObject = currentJsonObject.getJSONObject(JSON_OBJECT_SNIPPET);

                String videoId;
                try {
                    videoId = currentJsonObject.getJSONObject(JSON_OBJECT_ID).getString(JSON_STRING_VIDEO_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }
                String title = currentSnippetObject.getString(JSON_STRING_TITLE);
                String thumbUrl = currentSnippetObject.getJSONObject(JSON_OBJECT_THUMBNAILS).
                        getJSONObject(JSON_OBJECT_THUMBNAILS_DEFAULT).getString(JSON_STRING_THUMBNAILS_DEFAULT_URL);

                VideoItem videoItem = new VideoItem();
                videoItem.setVideoId(videoId);
                videoItem.setTitle(title);
                videoItem.setThumbUrl(thumbUrl);
                videoItems.add(videoItem);
            }
            return videoItems;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoItems;
    }

    public static String getNextPageToken(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.getString(JSON_STRING_NEXT_PAGE_TOKEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
