package com.lcgsen.master.app.inventory.module.login;

import com.lcgsen.master.app.inventory.base.App;
import com.lcgsen.master.app.inventory.base.BasePresenter;
import com.lcgsen.master.app.inventory.repository.bean.Result;
import com.lcgsen.master.app.inventory.repository.bean.UserBean;
import com.lcgsen.master.app.inventory.repository.remote.DataCallbackImp;

/**
 * author: xujiaji
 * created on: 2018/10/9 20:47
 * description:
 */
public class LoginPresenter extends BasePresenter<LoginContract.View,LoginModel> implements LoginContract.Presenter {
    @Override
    public void requestLogin(String account, String password) {
        view.displayProgress();
        model.catLogin(account, password, this, new DataCallbackImp<Result<UserBean>>() {

            @Override
            public void finished() {
                super.finished();
                view.dismissProgress();
            }

            @Override
            public void success(Result<UserBean> bean) {
                App.Login.in(bean.getData());
                view.loginSuccess();
            }
        });
    }

    @Override
    public void requestRegister(String account, String password) {
        view.displayProgress();
        model.catRegister(account, password, this, new DataCallbackImp<Result<UserBean>>() {

            @Override
            public void finished() {
                super.finished();
                view.dismissProgress();
            }


            @Override
            public void success(Result<UserBean> bean) {
                App.Login.in(bean.getData());
                view.loginSuccess();
            }
        });
    }
}
