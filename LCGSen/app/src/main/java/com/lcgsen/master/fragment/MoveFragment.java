package com.lcgsen.master.fragment;

import android.view.View;

public class MoveFragment extends HomeFragment implements View.OnClickListener {

    @Override
    protected void initView() {
        hostUrl = "https://m.yiybb.com/dianying/";
        super.initView();
        mThisPage = 1;
        listType = "List_15_";
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void setVisibility() {
        page.setVisibility(View.VISIBLE);
    }
}
