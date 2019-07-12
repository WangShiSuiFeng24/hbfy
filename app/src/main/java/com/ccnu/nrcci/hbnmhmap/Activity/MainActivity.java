package com.ccnu.nrcci.hbnmhmap.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.ccnu.nrcci.hbnmhmap.Fragment.InheritorFragment;
import com.ccnu.nrcci.hbnmhmap.Fragment.MapFragment;
import com.ccnu.nrcci.hbnmhmap.Fragment.MoreFragment;
import com.ccnu.nrcci.hbnmhmap.Fragment.ProjectFragment;
import com.ccnu.nrcci.hbnmhmap.R;

public class MainActivity extends Activity implements View.OnClickListener {

    Intent intent;

    //Button btn_return;
    Button btn_map;
    Button btn_inheritor;
    Button btn_project;
    Button btn_more;

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    Fragment map_fragment,project_fragment,inheritor_fragment,more_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        btn_map = (Button) findViewById(R.id.btn_map);
        btn_inheritor = (Button) findViewById(R.id.btn_inheritors);
        btn_project = (Button) findViewById(R.id.btn_project);
        btn_more = (Button) findViewById(R.id.btn_more);

        btn_map.setBackground(getResources().getDrawable(R.drawable.main_monitorclicked));
        btn_inheritor.setBackground(getResources().getDrawable(R.drawable.main_taskunclicked));
        btn_project.setBackground(getResources().getDrawable(R.drawable.main_alarmunclicked));
        btn_more.setBackground(getResources().getDrawable(R.drawable.main_moreunclicked));


        btn_map.setOnClickListener(this);
        btn_inheritor.setOnClickListener(this);
        btn_project.setOnClickListener(this);
        btn_more.setOnClickListener(this);

        map_fragment=new MapFragment();
        fragmentTransaction.add(R.id.fragment_content,map_fragment,"map").commit();

        /*intent = getIntent();
        String fragmentTag;
        if (intent.hasExtra("fragmentTag")){
            fragmentTag = intent.getStringExtra("fragmentTag");

            switch (fragmentTag){
                case "map":
                    map_fragment = new MapFragment();
                    fragmentTransaction.replace(R.id.fragment_content, map_fragment, "map").commit();
                    break;
                case "inheritor":
                    break;
                case "project":
                    alert_fragment = new AlertFragment();
                    fragmentTransaction.replace(R.id.fragment_content,alert_fragment,fragmentTag).commit();
                    btn_map.setBackground(getResources().getDrawable(R.drawable.main_monitorunclicked));
                    btn_alarm.setBackground(getResources().getDrawable(R.drawable.main_alarmclicked));
                    break;
                case "block":
                    break;
                case "more":
                    break;
                default:
                    map_fragment = new MapFragment();
                    fragmentTransaction.replace(R.id.fragment_content, map_fragment, "map").commit();
                    btn_map.setBackground(getResources().getDrawable(R.drawable.main_monitorunclicked));
                    btn_more.setBackground(getResources().getDrawable(R.drawable.main_moreclicked));
                    break;
            }
        }else {
            map_fragment = new MapFragment();
            fragmentTransaction.replace(R.id.fragment_content, map_fragment, "map").commit();
        }*/
    }

    @Override
    public void onClick(View v) {
        btn_map.setBackground(getResources().getDrawable(R.drawable.main_monitorunclicked));
        btn_inheritor.setBackground(getResources().getDrawable(R.drawable.main_taskunclicked));
        btn_project.setBackground(getResources().getDrawable(R.drawable.main_alarmunclicked));
        btn_more.setBackground(getResources().getDrawable(R.drawable.main_moreunclicked));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        map_fragment = fragmentManager.findFragmentByTag("map");
        inheritor_fragment = fragmentManager.findFragmentByTag("inheritor");
        project_fragment = fragmentManager.findFragmentByTag("project");
        more_fragment = fragmentManager.findFragmentByTag("more");

        if (map_fragment != null){
            fragmentTransaction.hide(map_fragment);
        }if (inheritor_fragment != null){
            fragmentTransaction.hide(inheritor_fragment);
        }if (project_fragment != null){
            fragmentTransaction.hide(project_fragment);
        }if (more_fragment != null){
            fragmentTransaction.hide(more_fragment);
        }

        switch (v.getId()){
            case R.id.btn_map:

                if (map_fragment == null) {
                    map_fragment = new MapFragment();
                }
                if (!map_fragment.isAdded()) {
                    fragmentTransaction.add(R.id.fragment_content, map_fragment, "map");
                } else {
                    fragmentTransaction.show(map_fragment);
                }
                //在fragment存在时设置点击刷新
                /*
                if (((MapFragment)map_fragment).map != null) {
                    ((MapFragment) map_fragment).refreshMap();
                }
                */
                btn_map.setBackground(getResources().getDrawable(R.drawable.main_monitorclicked));
                break;

            case R.id.btn_inheritors:
                if (inheritor_fragment == null){
                    inheritor_fragment = new InheritorFragment();
                }
                if (!inheritor_fragment.isAdded()) {
                    fragmentTransaction.add(R.id.fragment_content, inheritor_fragment, "inheritor");
                } else {
                    fragmentTransaction.show(inheritor_fragment);
                }
                btn_inheritor.setBackground(getResources().getDrawable(R.drawable.main_taskclicked));
                break;

            case R.id.btn_project:
                if (project_fragment== null){
                    project_fragment = new ProjectFragment();
                }
                if (!project_fragment.isAdded()) {
                    fragmentTransaction.add(R.id.fragment_content, project_fragment, "project");
                } else {
                    fragmentTransaction.show(project_fragment);
                }
                btn_project.setBackground(getResources().getDrawable(R.drawable.main_alarmclicked));
                break;


            case R.id.btn_more:
                if (more_fragment == null) {
                    more_fragment = new MoreFragment();
                }
                if (!more_fragment.isAdded()) {
                    fragmentTransaction.add(R.id.fragment_content, more_fragment, "more");
                } else {
                    fragmentTransaction.show(more_fragment);
                }
                btn_more.setBackground(getResources().getDrawable(R.drawable.main_moreclicked));
                break;
        }
        fragmentTransaction.commit();
    }
}
