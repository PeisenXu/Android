package com.lcgsen.master.app.inventory.module.about;

import android.support.v4.widget.SwipeRefreshLayout;

import com.lcgsen.master.app.inventory.base.BasePresenter;
import com.lcgsen.master.app.inventory.repository.bean.LicenseBean;
import com.lcgsen.master.app.inventory.repository.bean.Result;
import com.lcgsen.master.app.inventory.repository.remote.DataCallbackImp;

import java.util.List;

/**
 * author: xujiaji
 * created on: 2018/11/30 14:24
 * description:
 */
public class LicensePresenter extends BasePresenter<LicenseContract.View, LicenseModel> implements LicenseContract.Presenter {
    @Override
    public void requestLicense(SwipeRefreshLayout refreshLayout) {
        model.catLicense(this, new DataCallbackImp<Result<List<LicenseBean>>>(refreshLayout) {
            @Override
            public void success(Result<List<LicenseBean>> bean) {
                view.displayLicenseList(bean.getData());
            }
        });
    }
}
