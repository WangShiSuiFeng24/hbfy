package com.ccnu.nrcci.hbnmhmap.Util;

/**
 * Created by Administrator on 2017/11/27,0027.
 */

public class FileterAttr {
    private String name;
    private boolean isChecked;

    public FileterAttr(String name)
    {
        this.name=name;
    }
    public String getName(){return name;}

    public void setName(String name){this.name=name;}

    public boolean getIsChecked(){return isChecked;}

    public void setChecked(boolean isChecked){
        this.isChecked=isChecked;
    }
}
