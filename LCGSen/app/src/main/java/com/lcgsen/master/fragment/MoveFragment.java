package com.lcgsen.master.fragment;

import android.view.View;

/**
 * Created by tq on 2018/8/9.
 */

public class MoveFragment extends HomeFragment implements View.OnClickListener {

    @Override
    protected void initView() {
        hostUrl = "https://m.yiybb.com/dianying/";
        super.initView();
        mThisPage = 1;
        listType = "List_15_";
        mTvThisPage.setVisibility(View.VISIBLE);
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
