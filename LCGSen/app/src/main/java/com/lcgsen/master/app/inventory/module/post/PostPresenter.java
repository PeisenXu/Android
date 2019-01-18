package com.lcgsen.master.app.inventory.module.post;

import com.lcgsen.master.app.inventory.R;
import com.lcgsen.master.app.inventory.base.App;
import com.lcgsen.master.app.inventory.base.BasePresenter;
import com.lcgsen.master.app.inventory.helper.ToastHelper;
import com.lcgsen.master.app.inventory.repository.bean.Result;
import com.lcgsen.master.app.inventory.repository.bean.TodoTypeBean;
import com.lcgsen.master.app.inventory.repository.remote.DataCallbackImp;

/**
 * author: xujiaji
 * created on: 2018/10/10 22:02
 * description:
 */
public class PostPresenter extends BasePresenter<PostContract.View,PostModel> implements PostContract.Presenter {


    @Override
    public void requestAddTodo(TodoTypeBean.TodoListBean.TodoBean todoBean) {
        view.displayAddTodoIng();
        model.catAddTodo(todoBean, this, new DataCallbackImp<Result>() {

            @Override
            public void finished() {
                super.finished();
                view.displayAddTodoFinished();
            }

            @Override
            public void success(Result bean) {
                ToastHelper.success(App.getInstance().getString(R.string.success_add));
            }
        });
    }

    @Override
    public void requestUpdateTodo(TodoTypeBean.TodoListBean.TodoBean todoBean) {
        view.displayAddTodoIng();
        model.catUpdateTodo(todoBean, this, new DataCallbackImp<Result>() {
            @Override
            public void finished() {
                super.finished();
                view.displayAddTodoFinished();
            }

            @Override
            public void success(Result bean) {
                ToastHelper.success(App.getInstance().getString(R.string.success_edit));
            }
        });
    }
}
