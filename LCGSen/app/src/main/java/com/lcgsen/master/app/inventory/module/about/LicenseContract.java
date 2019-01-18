package com.lcgsen.master.app.inventory.module.about;

import android.support.v4.widget.SwipeRefreshLayout;

import com.lcgsen.master.app.inventory.base.PresenterLife;
import com.lcgsen.master.app.inventory.repository.bean.LicenseBean;
import com.lcgsen.master.app.inventory.repository.bean.Result;
import com.lcgsen.master.app.inventory.repository.remote.DataCallback;

import java.util.List;

import io.xujiaji.xmvp.contracts.XContract;

/**
 * author: xujiaji
 * created on: 2018/11/30 14:24
 * description:
 */
public class LicenseContract {
    interface View extends XContract.View {
        void displayLicenseList(List<LicenseBean> licenseBeans);
    }

    interface Presenter extends XContract.Presenter {
        void requestLicense(SwipeRefreshLayout refreshLayout);
    }

    interface Model extends XContract.Model {
        void catLicense(PresenterLife presenterLife, DataCallback<Result<List<LicenseBean>>> callback);
    }
}
