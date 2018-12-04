package com.boe.esl.socket.struct;

import java.util.List;

import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;

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
	 * 唤醒网关
	 */
	AWAKE((byte)0x02),
	/**
	 * 发送数据
	 */
	INFO((byte)0x08),
	/**
	 * 开始更新
	 */
	UPDATE((byte)0x04),
	/**
	 * 取消更新
	 */
	CANCEL((byte)0x05),
	/**
	 * 更新完毕
	 */
	DONE((byte)0x06),
	/**
	 * Bill Of Material 物料清单
	 */
	BOM((byte)0x07),
	/**
	 * 操作员上线
	 */
	LOGIN((byte)0x03),
	/**
	 * 心跳ping包
	 */
	PING((byte)0x09),
	/**
	 * 心跳pong包
	 */
	PONG((byte)0x0a);

	private byte value;
	
	private MessageType(byte value) {
		this.value = value;
	}
	
	public byte value() {
		return this.value;
	}
}
