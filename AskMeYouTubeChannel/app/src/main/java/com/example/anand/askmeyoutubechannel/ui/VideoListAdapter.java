package com.example.anand.askmeyoutubechannel.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.anand.askmeyoutubechannel.R;
import com.example.anand.askmeyoutubechannel.core.LoadDataListener;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class VideoListAdapter extends BaseAdapter {

    private ArrayList<VideoItem> mVideoItems;
    private Context mContext;
    private LayoutInflater mInflater;
    private LoadDataListener mLoadDataListener;
    private View mFooterView;

    private static final int MAX_CACHE_SIZE = 4 * 1024 * 1024;

    private LruCache<String, Bitmap> mThumbCache;

    public VideoListAdapter(Context context, LoadDataListener listener) {
        mContext = context;
        mLoadDataListener = listener;
        mInflater = LayoutInflater.from(mContext);
        mThumbCache = new LruCache<>(MAX_CACHE_SIZE);
    }

    public void destroy() {
        mContext = null;
        mInflater = null;
        mFooterView = null;
        mThumbCache.evictAll();
        mThumbCache = null;
    }

    public void setVideoItems(ArrayList<VideoItem> videoItems) {
        mVideoItems = videoItems;
    }

    @Override
    public int getCount() {
        if (mVideoItems != null) {
            return mVideoItems.size() + 1;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void loadNextPageSuccess() {
        notifyDataSetChanged();
    }

    public void loadNextPageFailure() {
        TextView noVideoText = (TextView) mFooterView.findViewById(R.id.load_more_no_video_text);
        ProgressBar noVideoProgressBar = (ProgressBar) mFooterView.findViewById(R.id.load_more_no_video_loading);
        noVideoText.setVisibility(View.VISIBLE);
        noVideoProgressBar.setVisibility(View.GONE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1) {
            mFooterView = mInflater.inflate(R.layout.list_view_footer, null);
//            mFooterView = new WeakReference<>(view).get();
            mFooterView.setTag(new FooterViewHolder());
            mLoadDataListener.onLoadRequest();
            return mFooterView;
        }
        ViewHolder viewHolder;
        if (null == convertView || convertView.getTag() instanceof FooterViewHolder) {
            convertView = mInflater.inflate(R.layout.list_item_video, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.video_title);
            viewHolder.thumb = (ImageView) convertView.findViewById(R.id.video_thumb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(mVideoItems.get(position).getTitle());
        String thumbUrl = mVideoItems.get(position).getThumbUrl();
        if (mThumbCache.get(thumbUrl) != null) {
            viewHolder.thumb.setImageBitmap(mThumbCache.get(thumbUrl));
        } else {
            viewHolder.thumb.setImageBitmap(null);
            try {
                new ImageDownLoaderTask(thumbUrl).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    private static class FooterViewHolder {

    }

    private static class ViewHolder {
        TextView title;
        ImageView thumb;
    }

    private class ImageDownLoaderTask extends AsyncTask {

        private String mURL;

        public ImageDownLoaderTask(String url) {
            mURL = url;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL url = new URL(mURL);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();

                return BitmapFactory.decodeStream(input);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            Bitmap bmp = (Bitmap) o;
            if (bmp != null) {
                if (mThumbCache == null) {
                    return;
                }
                mThumbCache.put(mURL, bmp);
                VideoListAdapter.this.notifyDataSetChanged();
            }
            super.onPostExecute(o);
        }
    }


}
