package com.boe.esl.model;

public enum APStatus {
    OFF_LINE(0,"离线"),
    UNNETWORKING(1,"未组网"),
    NETWORKING(2,"组网中"),
    ON_LINE(3,"在线");
    private int code;
    private String msg;
    APStatus(int code, String msg) {
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
