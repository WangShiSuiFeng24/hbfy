package com.ccnu.nrcci.hbnmhmap.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.ccnu.nrcci.hbnmhmap.Interfaces.OnAsyncListener;

import java.net.HttpURLConnection;
import java.net.URL;

public class ImageAsyncTask extends AsyncTask<String,Void,Bitmap> {
    private OnAsyncListener listener;
    @Override
    protected Bitmap doInBackground(String... params) {
        return getBitmapFromURL(params[0]);
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        if (listener!=null) {
            listener.asyncImgListener(result);
        }
        super.onPostExecute(result);
    }

    public Bitmap getBitmapFromURL(String  url){
        Bitmap bitmap = null;
        try {
            URL mURL=new URL(url);
            HttpURLConnection conn=(HttpURLConnection) mURL.openConnection();
            if(conn.getInputStream().equals("")){
                bitmap=null;
            }else{
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public void setOnImgAsyncTaskListener(OnAsyncListener listener){
        this.listener=listener;
    }
}
