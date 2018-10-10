package com.lcgsen.master;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lcgsen.master.adapter.VideoDetailAdapter;
import com.lcgsen.entity.DetailsBean;
import com.lcgsen.utils.MyStringRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends FragmentActivity {
    private static final String TAG = "DetailsActivity";
    @BindView(R.id.iv_da_img)
    ImageView mIvDaImg;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_updata)
    TextView mTvUpdata;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.da_recyView)
    RecyclerView mDaRecyView;
    private String mRequestUrl;
    private Matcher mMatcher;
    private List<DetailsBean> mData = new ArrayList<>();
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initWindow();

        setContentView(R.layout.video_details);
        ButterKnife.bind(this);
        initView();

        initData();
    }

    private void initView() {
        Intent intent = getIntent();
        mRequestUrl = intent.getStringExtra("requestUrl");
        String title = intent.getStringExtra("title");
        String img_url = intent.getStringExtra("img_url");
        mTvTitle.setText(title);
        Picasso.with(this).load(img_url).into(mIvDaImg);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mDaRecyView.setLayoutManager(mGridLayoutManager);
    }

    private void initData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        MyStringRequest stringRequest = new MyStringRequest(mRequestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e(TAG, response );
                String regEx = "<a href=\"/(Player_.+?_9_23.+?)\" >(.+?)</a></li>";
                //<a href="/Player_73525_23_14_1.html">BD中英双字</a>
                /*
                <div class="movie"><ul><div class="img"><div class="img-box-2"></div><img src="http://88.meenke.com/img_buyhi/201805/2018052876045761.jpg" alt="帝王攻略" border="0" onerror="this.src='/nopic.gif'"></div><h1>帝王攻略</h1><li>更新至：[17]</li><li>年　代：2018</li><li>类　型：<a href="/dhp_lianzai/Index.html" target="_blank">动画连载</a></li><li class="cksc"><a id="shoucang" href="#sc" onclick="shoucang('72893')">收藏</a></li></ul></div>
                 */

                Pattern pattern = Pattern.compile(regEx);
                mMatcher = pattern.matcher(response);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mMatcher.find()) {
                            String url = mMatcher.group(1);
                            Log.e(TAG, url);
                            String title = mMatcher.group(2);
                            DetailsBean dataBean = new DetailsBean();
                            dataBean.setTitle(title);
                            dataBean.setUrl(url);
                            mData.add(dataBean);
                        }
                    }
                }).start();
                VideoDetailAdapter adapter = new VideoDetailAdapter(DetailsActivity.this, mData, mGridLayoutManager);
                mDaRecyView.setAdapter(adapter);
                adapter.setItemClickListener(new VideoDetailAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String url = "https://m.yiybb.com/" + mData.get(position).getUrl();
                        Intent intent = new Intent(DetailsActivity.this, PlayActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                Toast.makeText(DetailsActivity.this, "网络不稳定!", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /** 修改状态栏为全透明开始 **/
            Window window = getWindow();
            window.setStatusBarColor(Color.TRANSPARENT);
            /** 修改状态栏为全透明结束 **/

            boolean isLight = true;
            View decor = window.getDecorView();
            int ui = decor.getSystemUiVisibility();
            if (isLight) {
                //light --> a|=b的意思就是把a和b按位或然后赋值给a,   按位或的意思就是先把a和b都换成2进制，然后用或操作，相当于a=a|b
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                //dark  --> &是位运算里面，与运算,  a&=b相当于 a = a&b,  ~非运算符
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(ui);
        }
    }
}
