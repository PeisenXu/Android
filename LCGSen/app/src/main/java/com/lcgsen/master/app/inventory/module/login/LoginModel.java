package com.lcgsen.master.app.inventory.module.login;

import com.lcgsen.master.R;
import com.lcgsen.master.app.inventory.base.App;
import com.lcgsen.master.app.inventory.base.PresenterLife;
import com.lcgsen.master.app.inventory.helper.InputHelper;
import com.lcgsen.master.app.inventory.helper.ToastHelper;
import com.lcgsen.master.app.inventory.repository.bean.Result;
import com.lcgsen.master.app.inventory.repository.bean.UserBean;
import com.lcgsen.master.app.inventory.repository.remote.CallbackHandler;
import com.lcgsen.master.app.inventory.repository.remote.DataCallback;
import com.lcgsen.master.app.inventory.repository.remote.Net;

/**
 * author: xujiaji
 * created on: 2018/10/9 20:47
 * description:
 */
public class LoginModel implements LoginContract.Model {
    @Override
    public void catLogin(String account, String password, PresenterLife presenterLife, DataCallback<Result<UserBean>> callback) {
        if (!isPassInput(account, password)) {
            callback.finished();
            return;
        }

        Net.getInstance().postLogin(account, password, CallbackHandler.getCallback(presenterLife, callback));
    }

    @Override
    public void catRegister(String account, String password, PresenterLife presenterLife, DataCallback<Result<UserBean>> callback) {
        if (!isPassInput(account, password)) {
            callback.finished();
            return;
        }
        Net.getInstance().postRegister(account, password, CallbackHandler.getCallback(presenterLife, callback));
    }

    private boolean isPassInput(String account, String password) {
        return true;
    }
}
