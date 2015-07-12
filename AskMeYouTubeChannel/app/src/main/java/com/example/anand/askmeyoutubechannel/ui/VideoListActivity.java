package com.example.anand.askmeyoutubechannel.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.anand.askmeyoutubechannel.R;
import com.example.anand.askmeyoutubechannel.core.Common;
import com.example.anand.askmeyoutubechannel.core.GetVideoListFromURLTask;
import com.example.anand.askmeyoutubechannel.core.HttpRequestListener;
import com.example.anand.askmeyoutubechannel.core.LoadDataListener;
import com.example.anand.askmeyoutubechannel.core.VideoListParser;

import java.util.ArrayList;


public class VideoListActivity extends Activity implements AdapterView.OnItemClickListener, HttpRequestListener, LoadDataListener {

    @Override
    public void onLoadRequest() {
        if (nextPageToken != null && !isCurrentlyLoading()) {
            GetVideoListFromURLTask getVideoListFromURLTask = new GetVideoListFromURLTask(this, Common.getUrlForNextPage(nextPageToken), false, this);
            try {
                setIsCurrentlyLoading(true);
                getVideoListFromURLTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                setIsCurrentlyLoading(false);
                e.printStackTrace();
            }
        }
    }

    public boolean isCurrentlyLoading() {
        return isCurrentlyLoading;
    }
    public void setIsCurrentlyLoading(boolean isLoading) {
        isCurrentlyLoading = isLoading;
    }

    @Override
    public void onFirstPageLoadSuccess(String result) {
        mVideoItems = VideoListParser.getVideoList(result);
        nextPageToken = VideoListParser.getNextPageToken(result);
        mVideoListAdapter.setVideoItems(mVideoItems);
        mVideoListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageLoadSuccess(String result) {
        mVideoItems = VideoListParser.loadNextPageVideoList(result, mVideoItems);
        nextPageToken = VideoListParser.getNextPageToken(result);
        mVideoListAdapter.setVideoItems(mVideoItems);
        mVideoListAdapter.loadNextPageSuccess();
        setIsCurrentlyLoading(false);
    }

    @Override
    public void onPageLoadFailure(boolean isFirstPage) {
        if (isFirstPage) {
            Toast.makeText(this, "An Error Occurred while retrieving Video List", Toast.LENGTH_SHORT).show();
        } else {
            mVideoListAdapter.loadNextPageFailure();
            setIsCurrentlyLoading(false);
        }
    }

    private ArrayList<VideoItem> mVideoItems;
    private ListView mVideoListView;

    private VideoListAdapter mVideoListAdapter;

    private String nextPageToken;

    private boolean isCurrentlyLoading = false;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setTitle("YouTube Videos");
        }
        //RequestQueue queue = Volley.newRequestQueue(this);
        mVideoListView = (ListView) findViewById(R.id.main_video_list);
        mVideoListAdapter = new VideoListAdapter(this, this);
        mVideoListView.setAdapter(mVideoListAdapter);
        mVideoListView.setOnItemClickListener(this);
        mVideoItems = new ArrayList<>();
        GetVideoListFromURLTask getVideoListFromURLTask = new GetVideoListFromURLTask(this, Common.API_LINK, true, this);
        getVideoListFromURLTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VideoItem item = mVideoItems.get(position);
        if (item.getVideoId() == null) {
            Toast.makeText(this, " Video id is Null", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, PlayVideoActivity.class);
        intent.putExtra(Common.VIDEO_ID, item.getVideoId());
        intent.putExtra(Common.TITLE, item.getTitle());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mVideoListAdapter != null) {
            mVideoListAdapter.destroy();
        }
        super.onDestroy();
    }

}
