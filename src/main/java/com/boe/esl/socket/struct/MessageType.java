package com.boe.esl.socket.struct;

/**
 * 消息类型枚举
 * @ClassName MessageType
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年8月22日 上午8:52:59
 * @lastUpdate 2018年8月22日 上午8:52:59
 */
public enum MessageType {
	/**
	 * 网关注册
	 */
	REGISTER((byte)0x01),
	/**
	 * 组网
	 */
	NETWORKING((byte)0x02),
	/**
	 * 心跳ping包
	 */
	PONG((byte)0x03),
	/**
	 * 新设备入网
	 */
	NETWORK((byte)0x04),
	/**
	 * 设备状态更新
	 */
	UPDATE((byte)0x05),
	/**
	 * 控制命令
	 */
	 CONTROL((byte)0x06),
	/**
	 * 显示更新
	 */
	DISPLAY((byte)0x07);

	private byte value;
	
	private MessageType(byte value) {
		this.value = value;
	}
	
	public byte value() {
		return this.value;
	}
}
