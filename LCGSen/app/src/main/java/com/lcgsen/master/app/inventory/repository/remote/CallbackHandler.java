package com.lcgsen.master.app.inventory.repository.remote;

import com.lcgsen.master.app.inventory.R;
import com.lcgsen.master.app.inventory.base.App;
import com.lcgsen.master.app.inventory.base.PresenterLife;
import com.lcgsen.master.app.inventory.helper.ToastHelper;
import com.lcgsen.master.app.inventory.repository.bean.Result;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;

import static com.lcgsen.master.app.inventory.repository.remote.Net.ZERO;

/**
 * author: xujiaji
 * created on: 2018/10/9 21:13
 * description:
 */
public class CallbackHandler {


    public static   <T extends Result> Callback<T> getCallback(final PresenterLife presenterLife, final DataCallback<T> dataCallback) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, retrofit2.Response<T> response) {
                if (presenterLife.isEnd()) return;
                dataCallback.finished();

                final Result result = response.body();
                if (result != null) {
                    if (result.getErrorCode() == ZERO) {
                        dataCallback.success(response.body());
                    } else {
                        dataCallback.fail(result.getErrorCode(), result.getErrorMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (presenterLife.isEnd()) return;
                dataCallback.finished();
                String errMsg;
                if (t instanceof UnknownHostException || t instanceof ConnectException) {
                    errMsg = App.getInstance().getString(R.string.please_check_network);
                } else if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    errMsg = App.getInstance().getString(R.string.link_out_time);
                } else {
                    errMsg = t.getMessage();
                }

                dataCallback.fail(-200, errMsg);
            }
        };
    }

}
