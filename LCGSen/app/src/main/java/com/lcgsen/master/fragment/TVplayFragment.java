package com.lcgsen.master.fragment;

import android.view.View;

public class TVplayFragment extends HomeFragment {

    @Override
    protected void initView() {
        hostUrl = "https://m.yiybb.com/dianshiju/";
        super.initView();
        mThisPage = 1;
        listType = "List_16_";
    }

    @Override
    protected void setVisibility() {
        page.setVisibility(View.VISIBLE);
    }
}
