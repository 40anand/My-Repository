package com.example.anand.askmeyoutubechannel.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.anand.askmeyoutubechannel.R;
import com.example.anand.askmeyoutubechannel.core.Common;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class PlayVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {

    private YouTubePlayerView mYouTubePlayerView;
    private String videoId;
    private String title;

    private ImageButton mShareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ActionBar actionBar = getActionBar();
        mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        mShareButton = (ImageButton) findViewById(R.id.share_button);
        mShareButton.setOnClickListener(this);
        videoId = getIntent().getStringExtra(Common.VIDEO_ID);
        title = getIntent().getStringExtra(Common.TITLE);
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            if (null != title) {
                actionBar.setTitle(title);
            }
        }
        mYouTubePlayerView.initialize(Common.API_KEY, this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mYouTubePlayerView.onConfigurationChanged(newConfig);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean start) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

       if (!start) {
            youTubePlayer.cueVideo(videoId);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onPlaying() {

        }

        @Override
        public void onSeekTo(int arg0) {

        }

        @Override
        public void onStopped() {

        }

    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {

        }

        @Override
        public void onLoaded(String arg0) {

        }

        @Override
        public void onLoading() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onVideoStarted() {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_button:
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                    intent.putExtra(Intent.EXTRA_TEXT, Common.YOUTUBE_VIDEO_VIEW_URL + videoId);

                    startActivity(Intent.createChooser(intent, "Share Video!"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
