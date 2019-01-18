package com.lcgsen.master.app.inventory.module.about;

import com.lcgsen.master.app.inventory.base.PresenterLife;
import com.lcgsen.master.app.inventory.repository.bean.LicenseBean;
import com.lcgsen.master.app.inventory.repository.bean.Result;
import com.lcgsen.master.app.inventory.repository.remote.CallbackHandler;
import com.lcgsen.master.app.inventory.repository.remote.DataCallback;
import com.lcgsen.master.app.inventory.repository.remote.Net;

import java.util.List;

/**
 * author: xujiaji
 * created on: 2018/11/30 14:24
 * description:
 */
public class LicenseModel implements LicenseContract.Model {
    @Override
    public void catLicense(PresenterLife presenterLife, DataCallback<Result<List<LicenseBean>>> callback) {
        Net.getInstance().getLicense(CallbackHandler.getCallback(presenterLife, callback));
    }
}
