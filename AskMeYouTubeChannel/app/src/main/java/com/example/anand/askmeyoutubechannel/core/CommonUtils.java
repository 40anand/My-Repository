package com.example.anand.askmeyoutubechannel.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class CommonUtils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return (networkInfo.isConnected());
        }
        return false;
    }
}
