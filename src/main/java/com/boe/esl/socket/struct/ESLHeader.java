package com.boe.esl.socket.struct;

import lombok.Data;

@Data
public class ESLHeader {
	private HeaderType code;//协议头
	private byte length;//数据长度
	private byte version= 0x01;//版本
	private MessageType type;//命令类型
	private byte pading = 0x00;//预留字段
}
