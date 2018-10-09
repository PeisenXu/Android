package com.lcgsen.master.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lcgsen.master.DetailsActivity;
import com.lcgsen.master.adapter.VideoHomeAdapter;
import com.lcgsen.entity.DataBean;
import com.lcgsen.master.R;
import com.lcgsen.utils.MyStringRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

import static android.content.ContentValues.TAG;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ll_page)
    LinearLayout page;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.up)
    Button mUp;
    @BindView(R.id.next)
    Button mNext;
    @BindView(R.id.tv_thisPage)
    TextView mTvThisPage;
    @BindView(R.id.home_page)
    Button mHomePage;
    private List<DataBean> mData = new ArrayList<>();
    private Matcher mMatcher;
    private GridLayoutManager mGridLayoutManager;
    public int mThisPage = 1;
    public String hostUrl = "https://m.yiybb.com";
    public String requestUrl = "";
    public String listType = "";
    private VideoHomeAdapter mVideoHomeAdapter;

    @Override
    protected void initView() {
        setVisibility();
        requestUrl = hostUrl;
        title.setVisibility(View.GONE);
        mTvThisPage.setVisibility(View.GONE);
        mGridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mUp.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mHomePage.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        MyStringRequest stringRequest = new MyStringRequest(getHosturl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String regEx = "<li><div class=li-box><div class=img-box></div><a href=\"(.+?)\"><img src=\"(.+?)\" onerror=\".+?\"><span class=back></span><span>(.+?)</span></div><P><a href=\".+?\" target=\"_blank\">(.+?)</a></P></li>";

                Pattern pattern = Pattern.compile(regEx);
                mMatcher = pattern.matcher(response);
                if (mData != null) {
                    mData.clear();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mMatcher.find()) {
                            DataBean dataBean = new DataBean();
                            dataBean.setDataNetWork(mMatcher.group(1));
                            dataBean.setDataImg(mMatcher.group(2));
                            dataBean.setDataName(mMatcher.group(4));
                            dataBean.setDataScore(mMatcher.group(3));
                            mData.add(dataBean);
                        }
                    }
                }).start();
                if (mVideoHomeAdapter == null) {
                    mVideoHomeAdapter = new VideoHomeAdapter(getContext(), mData, mGridLayoutManager);
                }
                mRecyclerView.setAdapter(mVideoHomeAdapter);
                mVideoHomeAdapter.notifyDataSetChanged();
                mTvThisPage.setText("第" + mThisPage + "页");
                Toast.makeText(getContext(), "第" + mThisPage + "页", Toast.LENGTH_SHORT).show();
                mVideoHomeAdapter.setItemClickListener(new VideoHomeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String url = mData.get(position).getDataNetWork();
                        String title = mData.get(position).getDataName();
                        String img_url = mData.get(position).getDataImg();
                        String requestUrl = getHosturl() + url;
                        //Toast.makeText(getContext(), requestUrl, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("img_url", img_url);
                        intent.putExtra("requestUrl", requestUrl);
                        startActivity(intent);
                    }
                });

                //Log.e(TAG, response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                mThisPage--;
                Toast.makeText(getContext(), "网络不稳定!加载失败!请稍后重试!", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }

    protected String getHosturl() {
        return requestUrl;
    }

    @Override
    protected int getLayout() {
        return R.layout.video_fragment_home;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up:
                mThisPage--;
                if (mThisPage > 1) {
                    requestUrl = hostUrl + listType + mThisPage + ".html";
                    initData();
                }
                break;
            case R.id.next:
                mThisPage++;
                if (mThisPage > 1) {
                    requestUrl = hostUrl + listType + mThisPage + ".html";
                    initData();
                }
                break;
            case R.id.home_page:
                mThisPage = 1;
                requestUrl = hostUrl;
                initData();
                break;
            default:
                break;
        }
    }

    protected void setVisibility() {
        page.setVisibility(View.GONE);
    }

}
