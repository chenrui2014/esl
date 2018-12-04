package com.boe.esl.model;

import lombok.ToString;

@ToString
public enum LabelStatus {

    OFF_LINE(0,"离线"),
    NETWORKING(1,"组网中"),
    REFRESH(2,"刷新中"),
    ON_LINE(3,"在线");
    private int code;
    private String msg;
    LabelStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
