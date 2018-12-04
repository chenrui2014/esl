package com.boe.esl.socket.struct;

/**
 * 帧头类型枚举
 * @ClassName HeaderType
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年8月22日 上午8:53:27
 * @lastUpdate 2018年8月22日 上午8:53:27
 */
public enum HeaderType {
	
	REQ((byte)0x55),
	RESP((byte)0x99);

	private byte value;

	private HeaderType(byte value) {
		this.value = value;
	}

	public byte value() {
		return this.value;
	}
}
