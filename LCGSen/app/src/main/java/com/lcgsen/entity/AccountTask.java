package com.lcgsen.entity;

import com.google.gson.annotations.SerializedName;

public class AccountTask {
    @SerializedName("ID")
    private String id;
    @SerializedName("TITLE")
    private String title;
    @SerializedName("CONTENT")
    private String content;
    @SerializedName("CREATE_TIME")
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
