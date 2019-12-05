package com.ccnu.nrcci.hbnmhmap.JavaBean;

/**
 * @project_name HBNMPepositories
 * @author:Jh
 * @time: 2019/8/1 16:42
 * @version:V1.0
 */
public class VideoListBean {

    private String video_name;
    private String video_place;
    private String video_intro;
    private String video_url;
    private String video_projectcover;

    public String getVideo_name(){
        return video_name;
    }

    public void setVideo_name(String video_name){
        this.video_name = video_name;
    }

    public String getVideo_place(){
        return video_place;
    }

    public void setVideo_place(String video_place){
        this.video_place = video_place;
    }

    public String getVideo_intro(){
        return video_intro;
    }

    public void setVideo_intro(String video_intro){
        this.video_intro = video_intro;
    }

    public String getVideo_url(){
        return video_url;
    }

    public void setVideo_url(String video_url){
        this.video_url = video_url;
    }

    public String getVideo_projectcover(){
        return video_projectcover;
    }

    public void setVideo_projectcover(String video_projectcover){
        this.video_projectcover = video_projectcover;
    }
}
