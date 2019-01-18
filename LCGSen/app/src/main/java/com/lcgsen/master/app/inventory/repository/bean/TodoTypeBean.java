package com.lcgsen.master.app.inventory.repository.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.lcgsen.master.app.inventory.module.main.MainContract;
import com.lcgsen.master.app.inventory.module.main.TodoAdapter;

import java.util.Comparator;
import java.util.List;

/**
 * author: xujiaji
 * created on: 2018/10/10 10:00
 * description:
 */
public class TodoTypeBean extends AbstractExpandableItem<TodoTypeBean.TodoListBean> implements MultiItemEntity {

    /**
     * doneList : [{"date":1533225600000,"todoList":[{"completeDate":1538841600000,"completeDateStr":"2018-10-07","content":"记得睡觉","date":1533225600000,"dateStr":"2018-08-03","id":57,"status":1,"title":"记录","type":0,"userId":5603}]}]
     * todoList : [{"date":1538841600000,"todoList":[{"completeDate":null,"completeDateStr":"","content":"","date":1538841600000,"dateStr":"2018-10-07","id":2852,"status":0,"title":"第二条清单","type":0,"userId":5603}]},{"date":1539792000000,"todoList":[{"completeDate":null,"completeDateStr":"","content":"","date":1539792000000,"dateStr":"2018-10-18","id":2853,"status":0,"title":"第三条清单","type":0,"userId":5603}]}]
     * type : 0
     */

    @SerializedName("type")
    private int type;
    private String title;
    @SerializedName("doneList")
    private List<TodoListBean> doneList;
    @SerializedName("todoList")
    private List<TodoListBean> todoList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<TodoListBean> getDoneList() {
        return doneList;
    }

    public void setDoneList(List<TodoListBean> doneList) {
        this.doneList = doneList;
    }

    public List<TodoListBean> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<TodoListBean> todoList) {
        this.todoList = todoList;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return TodoAdapter.TYPE_TODO_TYPE;
    }

    public static class TodoListBean  extends AbstractExpandableItem<TodoListBean.TodoBean> implements MultiItemEntity {
        /**
         * date : 1538841600000
         * todoList : [{"completeDate":null,"completeDateStr":"","content":"","date":1538841600000,"dateStr":"2018-10-07","id":2852,"status":0,"title":"第二条清单","type":0,"userId":5603}]
         */

        @SerializedName("date")
        private long date;
        @SerializedName("todoList")
        private List<TodoBean> todoList;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public List<TodoBean> getTodoList() {
            return todoList;
        }

        public void setTodoList(List<TodoBean> todoList) {
            this.todoList = todoList;
        }

        @Override
        public int getItemType() {
            return TodoAdapter.TYPE_TODO_DATE;
        }

        @Override
        public int getLevel() {
            return 0;
        }

        public static class TodoBean implements MultiItemEntity, Comparable<TodoBean>, Parcelable {
            /**
             * completeDate : null
             * completeDateStr :
             * content :
             * date : 1538841600000
             * dateStr : 2018-10-07
             * id : 2852
             * status : 0
             * title : 第二条清单
             * type : 0
             * userId : 5603
             */

            @SerializedName("completeDate")
            private long completeDate;
            @SerializedName("completeDateStr")
            private String completeDateStr;
            @SerializedName("content")
            private String content;
            @SerializedName("date")
            private long date;
            @SerializedName("dateStr")
            private String dateStr;
            @SerializedName("id")
            private int id;
            @SerializedName("status")
            private int status;
            @SerializedName("title")
            private String title;
            @SerializedName("type")
            private int type;
            @SerializedName("userId")
            private int userId;
            @SerializedName("priority")
            private int priority;

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public long getCompleteDate() {
                return completeDate;
            }

            public void setCompleteDate(long completeDate) {
                this.completeDate = completeDate;
            }

            public String getCompleteDateStr() {
                return completeDateStr;
            }

            public void setCompleteDateStr(String completeDateStr) {
                this.completeDateStr = completeDateStr;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public long getDate() {
                return date;
            }

            public void setDate(long date) {
                this.date = date;
            }

            public String getDateStr() {
                return dateStr;
            }

            public void setDateStr(String dateStr) {
                this.dateStr = dateStr;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            @Override
            public int getItemType() {
                return TodoAdapter.TYPE_TODO;
            }

            @Override
            public int compareTo(TodoBean o) {
                int a = priority;
                int b = o.priority;
                if (o.priority == MainContract.PRIORITY_NOTURGENT_NOTIMPORTANT || o.priority > MainContract.PRIORITY_URGENT_NOTIMPORTANT) {
                    b = 10;
                }
                if (priority == MainContract.PRIORITY_NOTURGENT_NOTIMPORTANT || priority > MainContract.PRIORITY_URGENT_NOTIMPORTANT) {
                    a = 10;
                }

                if (a == b) {
                    return 0;
                } else if (b > a) {
                    return -1;
                } else {
                    return 1;
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(this.completeDate);
                dest.writeString(this.completeDateStr);
                dest.writeString(this.content);
                dest.writeLong(this.date);
                dest.writeString(this.dateStr);
                dest.writeInt(this.id);
                dest.writeInt(this.status);
                dest.writeString(this.title);
                dest.writeInt(this.type);
                dest.writeInt(this.userId);
                dest.writeInt(this.priority);
            }

            public TodoBean() {
            }

            protected TodoBean(Parcel in) {
                this.completeDate = in.readLong();
                this.completeDateStr = in.readString();
                this.content = in.readString();
                this.date = in.readLong();
                this.dateStr = in.readString();
                this.id = in.readInt();
                this.status = in.readInt();
                this.title = in.readString();
                this.type = in.readInt();
                this.userId = in.readInt();
                this.priority = in.readInt();
            }

            public static final Parcelable.Creator<TodoBean> CREATOR = new Parcelable.Creator<TodoBean>() {
                @Override
                public TodoBean createFromParcel(Parcel source) {
                    return new TodoBean(source);
                }

                @Override
                public TodoBean[] newArray(int size) {
                    return new TodoBean[size];
                }
            };
        }
    }
}
