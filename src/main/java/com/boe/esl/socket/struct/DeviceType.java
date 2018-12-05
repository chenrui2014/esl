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
public enum DeviceType {
	/**
	 * 网关
	 */
	GATEWAY((byte)0x01),
	/**
     * 物料箱
	 */
	MBOX((byte)0x02),
	/**
	 * 货架
	 */
	SHELF((byte)0x03),
	/**
	 * 看板
	 */
	DASHBOAD((byte)0x04);

	private byte value;

	private DeviceType(byte value) {
		this.value = value;
	}
	
	public byte value() {
		return this.value;
	}
}
