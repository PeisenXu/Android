package com.lcgsen.master.fragment;

import android.view.View;

/**
 * Created by tq on 2018/8/9.
 */

public class TVplayFragment extends HomeFragment {

    @Override
    protected void initView() {
        hostUrl = "https://m.yiybb.com/dianshiju/";
        super.initView();
        mThisPage = 1;
        listType = "List_16_";
        mTvThisPage.setVisibility(View.VISIBLE);

    }

    @Override
    protected void setVisibility() {
        page.setVisibility(View.VISIBLE);
    }
}
