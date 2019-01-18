package com.lcgsen.master.app.inventory.module.login;

import com.lcgsen.master.app.inventory.base.PresenterLife;
import com.lcgsen.master.app.inventory.repository.bean.Result;
import com.lcgsen.master.app.inventory.repository.bean.UserBean;
import com.lcgsen.master.app.inventory.repository.remote.DataCallback;

import io.xujiaji.xmvp.contracts.XContract;

/**
 * author: xujiaji
 * created on: 2018/10/9 20:47
 * description:
 */
public class LoginContract {
    interface View extends XContract.View {
        void displayProgress();
        void dismissProgress();
        void switchToLogin();
        void switchToRegister();
        void loginSuccess();

        /**
         * 是否是登录页面
         */
        boolean isLoginPage();
    }

    interface Presenter extends XContract.Presenter {
        void requestLogin(String account, String password);
        void requestRegister(String account, String password);
    }

    interface Model extends XContract.Model {
        void catLogin(String account, String password, PresenterLife presenterLife, DataCallback<Result<UserBean>> callback);
        void catRegister(String account, String password, PresenterLife presenterLife, DataCallback<Result<UserBean>> callback);
    }
}
