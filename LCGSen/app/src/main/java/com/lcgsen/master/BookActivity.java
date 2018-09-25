package com.lcgsen.master;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BookActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sen_book);

        WebView displayWebview = (WebView) findViewById(R.id.Toweb);
        displayWebview.loadUrl("https://www.ixdzs.com/");

        WebSettings webSettings = displayWebview.getSettings();

        displayWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        displayWebview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        displayWebview.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        displayWebview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        displayWebview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        displayWebview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        displayWebview.getSettings().setAppCacheEnabled(true);//是否使用缓存
        displayWebview.getSettings().setDomStorageEnabled(true);//DOM Storage
        displayWebview.getSettings().setUserAgentString("User-Agent:Android");//设置用户代理，一般不用


        //设置可自由缩放网页、JS生效
        //webSettings.setSupportZoom(true);
        //webSettings.setBuiltInZoomControls(true);

        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
        displayWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
    }

}
