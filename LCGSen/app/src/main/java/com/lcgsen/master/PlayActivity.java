package com.lcgsen.master;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lcgsen.utils.EscapeUtils;
import com.lcgsen.utils.MyStringRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tq on 2018/8/16.
 */

public class PlayActivity extends AppCompatActivity {
    private static final String TAG = "PlayActivity";
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.progressbar)
    ProgressBar mProgressbar;
    private String mUrl;
    private Matcher mMatcher;
    private String mFirst_url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        MyStringRequest stringRequest = new MyStringRequest(mUrl, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                String regEx = "unescape\\(\"(.+?)\",0,0\\)";
                Pattern pattern = Pattern.compile(regEx);
                mMatcher = pattern.matcher(response);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mMatcher.find()) {
                            Log.e(TAG, mMatcher.group(1));
                            mFirst_url = mMatcher.group(1);
                            final String requestUrl = "javascript:getUrl('" + mFirst_url + "')";
                            Log.e(TAG, requestUrl);
                            String unescape = EscapeUtils.unescape(requestUrl);
                            Log.e(TAG, "unescape: " + unescape);
                            mWebView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mWebView.loadUrl(requestUrl);
                                }
                            });
                        }
                    }
                }).start();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                Toast.makeText(PlayActivity.this, "网络不稳定!加载失败!请稍后重试!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

    }


    @SuppressLint("JavascriptInterface")
    private void initView() {
        mUrl = getIntent().getStringExtra("url");
        mWebView.loadUrl("file:///android_asset/index.html");
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "android");
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        //支持屏幕缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);


    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成

            mProgressbar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            mProgressbar.setVisibility(View.VISIBLE);
            Log.e(TAG, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("ansen", "拦截url:" + url);
            if (url.equals("http://www.google.com/")) {
                Toast.makeText(PlayActivity.this, "国内不能访问google,拦截该url", Toast.LENGTH_LONG).show();
                return true;//表示我已经处理过了
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }


    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView mWebView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(mWebView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i("ansen", "网页标题:" + title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressbar.setProgress(newProgress);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*//释放资源
        mWebView.destroy();
        mWebView = null;*/
    }


}
