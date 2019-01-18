package com.lcgsen.master.app.inventory.module.main;

import com.lcgsen.master.app.inventory.base.PresenterLife;
import com.lcgsen.master.app.inventory.repository.bean.DailyBean;
import com.lcgsen.master.app.inventory.repository.bean.Result;
import com.lcgsen.master.app.inventory.repository.bean.TodoTypeBean;
import com.lcgsen.master.app.inventory.repository.remote.CallbackHandler;
import com.lcgsen.master.app.inventory.repository.remote.DataCallback;
import com.lcgsen.master.app.inventory.repository.remote.Net;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * author: xujiaji
 * created on: 2018/10/10 10:06
 * description:
 */
public class MainModel implements MainContract.Model {

    public static final SimpleDateFormat dailyFormat = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);

    @Override
    public void catTodo(int type, PresenterLife presenterLife, DataCallback<Result<TodoTypeBean>> callback) {
        Net.getInstance().getTodoByType(type, CallbackHandler.getCallback(presenterLife, callback));
    }

    @Override
    public void catUpdateTodo(TodoTypeBean.TodoListBean.TodoBean todoBean, PresenterLife presenterLife, DataCallback<Result> callback) {
        Net.getInstance().postUpdateTodo(todoBean, CallbackHandler.getCallback(presenterLife, callback));
    }

    @Override
    public void catDelTodo(int id, PresenterLife presenterLife, DataCallback<Result> callback) {
        Net.getInstance().postDelTodo(id, CallbackHandler.getCallback(presenterLife, callback));
    }

    @Override
    public void catDailyList(PresenterLife presenterLife, DataCallback<Result<Map<String, DailyBean>>> callback) {
        Net.getInstance().getDailyList(CallbackHandler.getCallback(presenterLife, callback));
    }
}
