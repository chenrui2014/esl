package com.boe.esl.model;

import lombok.ToString;

@ToString
public enum OperationType {
    REFRESH(0,"刷新"),
    CONTROLLE(1,"控制"),
    INFO(2,"获取信息");
    private int code;
    private String msg;
    OperationType(int code, String msg) {
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
