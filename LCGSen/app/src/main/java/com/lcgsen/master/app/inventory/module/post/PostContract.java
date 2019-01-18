package com.lcgsen.master.app.inventory.module.post;

import com.lcgsen.master.app.inventory.base.PresenterLife;
import com.lcgsen.master.app.inventory.module.main.MainContract;
import com.lcgsen.master.app.inventory.repository.bean.Result;
import com.lcgsen.master.app.inventory.repository.bean.TodoTypeBean;
import com.lcgsen.master.app.inventory.repository.remote.DataCallback;

import io.xujiaji.xmvp.contracts.XContract;

/**
 * author: xujiaji
 * created on: 2018/10/10 22:02
 * description:
 */
public class PostContract {
    interface View extends XContract.View {
        void hidePage();

        void showChooseCalender();
        void hideChooseCalender();
        void showChooseTodoCategory();
        void hideChooseTodoCategory();

        void showChoosePriority();
        void hideChoosePriority();

        void showEditContent();
        void hideEditContent();

        void displayAddTodoIng();
        void displayAddTodoFinished();
    }

    interface Presenter extends XContract.Presenter {
        void requestAddTodo(TodoTypeBean.TodoListBean.TodoBean todoBean);
        void requestUpdateTodo(TodoTypeBean.TodoListBean.TodoBean todoBean);
    }

    interface Model extends XContract.Model, MainContract.Model {
        void catAddTodo(TodoTypeBean.TodoListBean.TodoBean todoBean, PresenterLife presenterLife, DataCallback<Result> callback);
    }
}
