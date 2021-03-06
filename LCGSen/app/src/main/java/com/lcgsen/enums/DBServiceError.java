package com.lcgsen.enums;

public enum DBServiceError {
    DB_SERVICE_URL(80, "https://www.xupeisen.com"),
    DB_SERVICE_OK(10000, ""),
    DB_SERVICE_ERROR(10001, "InterServer Error"),
    DB_SERVICE_LOGIN_NO_NAME(10002, "请输入邮箱"),
    DB_SERVICE_LOGIN_NO_PWD(10003, "请输入密码"),

    DB_SERVICE_LOGIN_SUCCESS(20000, "登陆成功"),
    DB_SERVICE_LOGIN_ERROR(20001, "账号或密码错误"),

    COPY_SUCCESS(30001, "复制成功，快去粘贴吧"),

    BEI_YONG(90000, "备用");

    private String msg;
    private int code;

    DBServiceError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getCode() {
        return this.code;
    }

}
