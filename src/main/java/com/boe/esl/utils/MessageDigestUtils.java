package com.boe.esl.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestUtils {

	/**
	 * 算法类型枚举
	 * @description：功能描述
	 * @author： chenruixiang
	 * @created： 2018年5月31日上午10:17:54
	 * @version：2.x
	 */
	enum EncType {
		MD5("MD5", 1), SHA1("SHA-1", 2), SHA256("SHA-256", 3), SHA384("SHA-384", 4);
		private String name;
		private int index;

		// 构造方法
		private EncType(String name, int index) {
			this.name = name;
			this.index = index;
		}

		// 普通方法
		public static String getName(int index) {
			for (EncType c : EncType.values()) {
				if (c.getIndex() == index) {
					return c.name;
				}
			}
			return null;
		}

		// get set 方法
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	public static String sha256(String strSrc) {

		return encrypt(strSrc, EncType.SHA256);
	}

	public static String sha1(String strSrc) {

		return encrypt(strSrc, EncType.SHA1);
	}

	public static String md5(String strSrc) {

		return encrypt(strSrc, EncType.MD5);
	}

	public static String sha384(String strSrc) {

		return encrypt(strSrc, EncType.SHA384);
	}

	public static String encrypt(String strSrc, EncType encType) {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance(encType.getName());
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			System.out.println("签名失败！");
			return null;
		}
		return strDes;
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
}
