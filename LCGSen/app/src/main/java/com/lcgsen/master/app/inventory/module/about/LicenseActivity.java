package com.lcgsen.master.app.inventory.module.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcgsen.master.app.inventory.R;
import com.lcgsen.master.app.inventory.base.BaseActivity;
import com.lcgsen.master.app.inventory.helper.EmptyViewHelper;
import com.lcgsen.master.app.inventory.helper.ToolbarHelper;
import com.lcgsen.master.app.inventory.repository.bean.LicenseBean;
import com.lcgsen.master.app.inventory.util.NetUtil;

import java.util.List;


/**
 * author: xujiaji
 * created on: 2018/8/30 19:28
 * description:
 */
public class LicenseActivity extends BaseActivity<LicensePresenter> implements LicenseContract.View, SwipeRefreshLayout.OnRefreshListener {

    private LicenseAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private Toolbar mToolbar;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, LicenseActivity.class));
    }

    @Override
    public int layoutId() {
        return R.layout.activity_license;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ToolbarHelper.initTranslucent(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onInitCircle() {
        super.onInitCircle();
        mAdapter = new LicenseAdapter();
        mRecyclerView = findViewById(R.id.list);
        mRefreshLayout = findViewById(R.id.refresh);
        mToolbar = findViewById(R.id.toolbar);

        mAdapter.bindToRecyclerView(mRecyclerView);
        EmptyViewHelper.initEmpty(mRecyclerView);
        mToolbar.setTitle(R.string.opean_source_libraries);
        ToolbarHelper.initFullBar(mToolbar, this);

        presenter.requestLicense(mRefreshLayout);
    }

    @Override
    public void onListenerCircle() {
        super.onListenerCircle();
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NetUtil.systemBrowserOpen(LicenseActivity.this, mAdapter.getData().get(position).getLink());
            }
        });
    }

    @Override
    public void displayLicenseList(List<LicenseBean> licenseBeans) {
        mAdapter.setNewData(licenseBeans);
    }

    @Override
    public void onRefresh() {
        presenter.requestLicense(mRefreshLayout);
    }
}
