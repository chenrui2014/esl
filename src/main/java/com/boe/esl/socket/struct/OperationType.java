package com.boe.esl.socket.struct;

import lombok.ToString;

@ToString
public enum OperationType {
    /**
     * 灯灭
     */
    LIGHT_OFF((byte)0x00),
    /**
     * 灯1亮
     */
    LIGHT1_ON((byte)0x01),
    /**
     * 灯2亮
     */
    LIGHT2_ON((byte)0x02),
    /**
     * 灯3亮
     */
    LIGHT3_ON((byte)0x03),
    /**
     * 状态不变
     */
    NO_CHANGE((byte)0xFF);

    private byte value;

    private OperationType(byte value) {
        this.value = value;
    }

    public byte value() {
        return this.value;
    }
}
