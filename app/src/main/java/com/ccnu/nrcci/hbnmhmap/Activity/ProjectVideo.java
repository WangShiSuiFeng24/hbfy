package com.ccnu.nrcci.hbnmhmap.Activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccnu.nrcci.hbnmhmap.R;
import com.ccnu.nrcci.hbnmhmap.Util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jzvd.JzvdStd;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import cn.jzvd.Jzvd;


public class ProjectVideo extends AppCompatActivity {

//    public void onBackPressed(){
//        finish();
//        Intent intent = new Intent(ProjectVideo.this,ProjectActivity.class);
//        startActivity(intent);
//    }

    /*static String url = "http://202.114.41.165:8080";
    StringBuilder resource_video;
    String projectcode;
    List<String> video = new ArrayList<>();
    LinearLayout group;
    MediaController mediaController;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if (msg.what == 1){
                for (int i = 0;i<video.size();i++){
                    Log.i("ffff","aaaaa");
                    VideoView videoView = new VideoView(getApplicationContext());
                    videoView.setMediaController(mediaController);
                    mediaController.setMediaPlayer(videoView);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
                    lp.setMargins(0,100,0,100);
                    videoView.setLayoutParams(lp);
                    videoView.setVideoURI(Uri.parse(video.get(i)));//部分视频无法播放是因为数据库中的视频路径数据写错了，坑爹
                    *//*videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                        }
                    });*//*
                    group.addView(videoView);
                    videoView.requestFocus();
                    //videoView.start();
                }
            }

        }
    };*/

                /*VideoView videoView = new VideoView(getApplicationContext());
                videoView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                group.addView(videoView);

                videoView.setVideoURI(Uri.parse("http://202.114.41.165:8080/荆楚国家级非遗精粹/视频/黄鹤楼传说.flv"));
                videoView.setMediaController(new MediaController(getApplicationContext()));
                mediaController.setMediaPlayer(videoView);
                videoView.requestFocus();
                videoView.start();*/
                /*for (int i = 0;i<video.size();i++){
                    VideoView videoView = new VideoView(getApplicationContext());
                    videoView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    group.addView(videoView);
                    mediaController = new MediaController(getApplicationContext());
                    Uri uri = Uri.parse(video.get(i));
                    videoView.setVideoURI(uri);
                    videoView.setMediaController(mediaController);
                    mediaController.setMediaPlayer(videoView);
                    videoView.requestFocus();
                    videoView.start();
                }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project_video);

//        Bundle bundle = getIntent().getExtras();
//        String url = bundle.getString("url");
//        String videoname = bundle.getString("name");
//        Uri projectcover = Uri.parse(bundle.getString("projectcover")) ;
        JzvdStd jzvdStd = (JzvdStd)findViewById(R.id.jz_video);
        jzvdStd.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4","yyy");
        //jzvdStd.thumbImageView.setImageURI("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
//        MyJzvdStd jzvdStd = (MyJzvdStd) findViewById(R.id.jz_video);
//        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
//        jcVideoPlayerStandard.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, videoname);
//        jcVideoPlayerStandard.startWindowFullscreen();





        /*group = (LinearLayout) findViewById(R.id.vedioviewgroup);
        mediaController = new MediaController(this);
        requestUsingHttpURLConnectionGetVideoByProjectCode();*/

    }


    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    /*public void requestUsingHttpURLConnectionGetVideoByProjectCode(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://202.114.41.165:8080/FYProject/servlet/GetVideosByProjectCode"); // 声明一个URL,注意——如果用百度首页实验，请使用https
                    connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                    connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                    connection.setConnectTimeout(8000); // 设置连接建立的超时时间
                    connection.setReadTimeout(8000); // 设置网络报文收发超时时间
                    InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    resource_video = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        resource_video.append(line);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray a = new JSONArray(resource_video.toString());
                    for (int i = 0;i < a.length();i++) {
                        JSONObject b = a.getJSONObject(i);

                        if (b.getString("LinkCode").equals(projectcode)) {
                            video.add(url + b.getString("ResourceUrl"));
                        }
                    }

                    Message msg = new Message();
                    msg.what = 1;
                    Log.i("AA","1111111");
                    handler.sendMessage(msg);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
}
