package com.boe.esl.socket.struct;

public enum ResultStatus {

	/**
	 * OK
	 */
	OK((byte) 0x00),
	/**
	 * 服务器错误
	 */
	SERVER_ERROR((byte)0x10),
	/**
	 * 网关错误
	 */
	AP_ERROR((byte)0x20),
	
	/**
	 * 终端错误
	 */
	ENTPORT_ERROR((byte)0x30),
	/**
	 * 通用错误
	 */
	COMMON_ERROR((byte)0x40);
	private byte value;

	private ResultStatus(byte value) {
		this.value = value;
	}

	public byte value() {
		return this.value;
	}
}
