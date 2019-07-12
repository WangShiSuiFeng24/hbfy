package com.ccnu.nrcci.hbnmhmap.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ccnu.nrcci.hbnmhmap.R;

public class MapFragment extends android.app.Fragment {
    private WebView wv_map;
    private String url="file:////android_asset/FYMap/mapPhone.html";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        wv_map=view.findViewById(R.id.wv_map);
        WebSettings webSettings=wv_map.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wv_map.loadUrl(url);

        return view;
    }
}
