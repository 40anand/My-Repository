package com.example.anand.askmeyoutubechannel.core;

public interface HttpRequestListener {
    void onFirstPageLoadSuccess(String result);

    void onPageLoadSuccess(String result);

    void onPageLoadFailure(boolean isFirstPage);
}
