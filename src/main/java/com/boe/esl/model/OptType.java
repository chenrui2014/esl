package com.boe.esl.model;

import lombok.ToString;

@ToString
public enum OptType {

    LIGHT_OFF((byte)0x00,"灯灭"),
    LIGHT1_ON((byte)0x01,"灯1亮"),
    LIGHT2_ON((byte)0x02,"灯2亮"),
    LIGHT3_ON((byte)0x03,"灯3亮"),
    NO_CHANGE((byte)0xFF,"不变");
    private byte code;
    private String msg;
    OptType(byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public void setCode(byte code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
