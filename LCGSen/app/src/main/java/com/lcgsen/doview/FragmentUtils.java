package com.lcgsen.doview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.lcgsen.master.R;

public class FragmentUtils extends Fragment {

    private TextView tv;
    private WebView webView;

    public static FragmentUtils newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        FragmentUtils fragment = new FragmentUtils();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        Object nameObj = bundle.get("name");
        View view;
        if (bundle != null && nameObj != null) {
            if ("首页".equalsIgnoreCase(nameObj.toString())) {
                view = inflater.inflate(R.layout.fragment_one, container, false);
            } else {
                view = inflater.inflate(R.layout.fragment, container, false);
            }
        } else {
            view = inflater.inflate(R.layout.fragment, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        Object nameObj = bundle.get("name");
        if (bundle != null && nameObj != null) {
            if ("首页".equalsIgnoreCase(nameObj.toString())) {
                webView = (WebView) view.findViewById(R.id.webview);
                webView.loadUrl("http://www.baidu.com");
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);//允许使用js

                /**
                 * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
                 * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
                 * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
                 * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
                 */
                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

                // 支持屏幕缩放
                webSettings.setSupportZoom(true);
                webSettings.setBuiltInZoomControls(true);

                // 不显示webview缩放按钮
                // webSettings.setDisplayZoomControls(false);
            } else {
                tv = (TextView) view.findViewById(R.id.fragment_test_tv);
                String name = nameObj.toString();
                tv.setText(name);
            }
        }
    }

}
