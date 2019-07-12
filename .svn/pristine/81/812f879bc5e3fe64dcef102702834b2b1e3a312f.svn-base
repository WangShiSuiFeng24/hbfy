package com.ccnu.nrcci.hbnmhmap.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.ccnu.nrcci.hbnmhmap.Interfaces.OnAsyncListener;
import com.ccnu.nrcci.hbnmhmap.R;

import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoader {
    private ImageView iv;
    private String url;
    private LruCacheUtils mCacheUtils;

    public ImageLoader() {
        mCacheUtils=new LruCacheUtils();
    }
    /**
     * @author mikyou
     * 实现的主要思路:
     * 首先、加载图片的时候，先去LruCache缓存中根据传入的url作为key去取相应的Bitmap对象
     * ，如果缓存中存在相应的key对应的value,那么就直接取出key对应缓存中的Bitmap对象
     * 并设置给ImageView，如果缓存中没有，那么就需要通过异步加载请求网络中的数据和图片信息，
     * 然后通过监听器中的asyncImgListener回调方法将网络请求得到的Bitmap对象，首先得通过iv.getTag()
     * 比较url如果对应就将该Bitmap对象设置给iv,并且还需要将这个Bitmap对象和相应的url以key-value形式
     * 通过put方法，加入LruCache缓存中。
     * */
    public void showImageByAsyncTask(final ImageView iv, final String url){
        //首先，从缓存中读取图片，如果有就直接使用缓存，如果没有就直接加载网络图片
        Bitmap bitmap=mCacheUtils.getBitmapFromCache(url);
        //Log.d("url", url);
        if (bitmap==null) {//表示缓存中没有，就去访问网络下载图片，并记住将下载到的图片放入缓存中
            ImageAsyncTask imageAsyncTask=new ImageAsyncTask();
            imageAsyncTask.execute(url);
            imageAsyncTask.setOnImgAsyncTaskListener(new OnAsyncListener() {
               /* @Override
                public void asyncListener(List<Course> mCourseList) {

                }*/
                @Override
                public void asyncImgListener(Bitmap bitmap) {//图片请求网络数据的回调方法
                    if (iv.getTag().equals(url)&&bitmap!=null) {//判断url和iv是否对应
                        iv.setImageBitmap(bitmap);
                        Log.d("addLru", "网络加载并加入缓存--->"+url);
                        mCacheUtils.addBitmapToCache(url, bitmap);//由于是网络请求得到的数据，所以缓存中肯定没有，所以还需要将该Bitmap对象加入到缓存中
                    }else if(bitmap==null){
                        //bitmap=BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/iamge_background64.png"));
                        iv.setImageResource(R.drawable.image_background64);
                    }

                }
            });
        }else{//否则就直接从缓存中获取


                iv.setImageBitmap(bitmap);//直接读取缓存中的Bitmap对象
                Log.d("getLru", "url读出缓存--->"+url);


        }

    }



    //HttpURLConnection网络请求方式来得到网络图片输入流，并且将输入流转换成一个Bitmap对象
    public Bitmap getBitmapFromURL(String  url){
        Bitmap bitmap = null;
        try {
            URL mURL=new URL(url);
            HttpURLConnection conn=(HttpURLConnection) mURL.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
