package com.boe.esl.model;

public enum OrderStatus {

	UNSENT((byte)0X00),
	SENTED((byte)0x01);
	
	private byte value;

	private OrderStatus(byte value) {
		this.value = value;
	}

	public byte value() {
		return this.value;
	}
}
