package com.boe.esl.socket;

public enum CharacterSet {
	UTF_8(1,"utf-8"),
	GBK(2,"gbk"),
	GB2312(3,"gb2312"),
	ISO_8859_1(4,"ISO-8859-1");
	
	private int value;
	private String name;
	
	CharacterSet(int value, String name) {
		this.name = name;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
