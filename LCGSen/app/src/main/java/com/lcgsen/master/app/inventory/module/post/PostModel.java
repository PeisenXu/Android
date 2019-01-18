package com.lcgsen.master.app.inventory.module.post;

import com.lcgsen.master.app.inventory.base.PresenterLife;
import com.lcgsen.master.app.inventory.module.main.MainModel;
import com.lcgsen.master.app.inventory.repository.bean.Result;
import com.lcgsen.master.app.inventory.repository.bean.TodoTypeBean;
import com.lcgsen.master.app.inventory.repository.remote.CallbackHandler;
import com.lcgsen.master.app.inventory.repository.remote.DataCallback;
import com.lcgsen.master.app.inventory.repository.remote.Net;

/**
 * author: xujiaji
 * created on: 2018/10/10 22:02
 * description:
 */
public class PostModel extends MainModel implements PostContract.Model {
    @Override
    public void catAddTodo(TodoTypeBean.TodoListBean.TodoBean todoBean, PresenterLife presenterLife, DataCallback<Result> callback) {
        Net.getInstance().postAddTodo(todoBean, CallbackHandler.getCallback(presenterLife, callback));
    }
}
