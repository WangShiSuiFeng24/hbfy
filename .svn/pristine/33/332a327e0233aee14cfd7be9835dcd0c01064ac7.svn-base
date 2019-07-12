package com.ccnu.nrcci.hbnmhmap.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leiming on 2017/11/27,0027.
 * 单条筛选条件类
 */

public class FilterItem {
    private String name;
    private List<FileterAttr> filterContent;
    private boolean nameIsChecked;

    /*public FilterItem(String name,List<FileterAttr> filterContent){
        this.name=name;
        this.filterContent=filterContent;
        this.nameIsChecked=false;
    }*/
    public FilterItem(String name, List<String> orgList){
        this.name=name;
        List<FileterAttr> tem=new ArrayList<FileterAttr>();
        for (String i:orgList) {
            tem.add(new FileterAttr(i));
        }
        this.filterContent=tem;
    }

    public String getName(){return name;}

    public void setName(String name){this.name=name;}

    public List<FileterAttr> getFilterContent(){return filterContent;}

    public void setFilterContent(List<FileterAttr> list){this.filterContent=list;}

    public Boolean isNameIsChecked(){return nameIsChecked;}

    public void setNameIsChecked(boolean nameIsChecked){this.nameIsChecked=nameIsChecked;}

}
