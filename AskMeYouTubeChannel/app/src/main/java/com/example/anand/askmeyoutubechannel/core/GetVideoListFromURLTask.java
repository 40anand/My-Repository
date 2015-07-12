package com.example.anand.askmeyoutubechannel.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetVideoListFromURLTask extends AsyncTask {
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private String mUriString;
    private String mResultString;
    private HttpRequestListener mHttpRequestListener;
    private boolean isFirstPage;

    public GetVideoListFromURLTask(Context context, String uri, boolean isFirstPage, HttpRequestListener httpRequestListener) {
        mContext = context;
        mUriString = uri;
        this.isFirstPage = isFirstPage;
        mHttpRequestListener = httpRequestListener;
        mResultString = null;
        mProgressDialog = new ProgressDialog(mContext);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        if (CommonUtils.isNetworkConnected(mContext)) {
            mResultString = getData(mUriString);
            return mResultString;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        if (mProgressDialog != null && isFirstPage) {
            mProgressDialog.setMessage("Please Wait");
            mProgressDialog.show();
        }
        super.onPreExecute();
    }


    private String getData(String uri) {

        BufferedReader reader = null;

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            Log.d("anand", sb.toString());
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }

    @Override
    protected void onPostExecute(Object o) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (mResultString != null) {
            if(isFirstPage) {
                mHttpRequestListener.onFirstPageLoadSuccess(mResultString);
            } else {
                mHttpRequestListener.onPageLoadSuccess(mResultString);
            }
        } else {
            mHttpRequestListener.onPageLoadFailure(isFirstPage);
        }
        super.onPostExecute(o);
    }
}
