package com.boe.esl.socket;

import com.boe.esl.model.Goods;
import com.boe.esl.model.Label;
import com.boe.esl.socket.struct.ResultStatus;

import com.boe.esl.vo.UpdateVO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ESLSocketUtils {


	public static ByteBuf convertUpdateToByte(UpdateVO updateVO){
		ByteBuf infoByte = Unpooled.buffer(NettyConstant.REQ_UPDATE_LENGTH);
		byte[] updateBytes = new byte[NettyConstant.REQ_UPDATE_LENGTH];
		try {
			System.arraycopy(updateVO.getSid().getBytes(), 0, updateBytes, 0, updateVO.getSid().getBytes().length);
			System.arraycopy(updateVO.getBarCode().getBytes(), 0, updateBytes, 6, updateVO.getBarCode().getBytes().length);
			System.arraycopy(updateVO.getMaterialName().getBytes(), 0, updateBytes, 21, updateVO.getMaterialName().getBytes().length);
			System.arraycopy(updateVO.getMaterialNum().getBytes(), 0, updateBytes, 36, updateVO.getMaterialNum().getBytes().length);
		}catch (Exception e) {
			log.error("字符编码转换错误", e.getMessage());
		}

		infoByte.writeBytes(updateBytes);
		return infoByte;
	}
	/**
	 * 将商品信息转换为ByteBuf
	 * @Title convertLabelToByte
	 * @Description TODO()
	 * @params @param goods
	 * @params @return
	 * @return ByteBuf
	 * @throws 
	 * @create 2018年8月24日 上午9:42:23
	 * @lastUpdate 2018年8月24日 上午9:42:23
	 */
	public static ByteBuf convertGoodsToByte(Goods goods) {
		ByteBuf infoByte = Unpooled.buffer(NettyConstant.REQ_INFO_LENGTH);
		byte[] goodsBytes = new byte[NettyConstant.REQ_INFO_LENGTH-8];//去掉价格double类型长度，double类型单独写
		Label label = goods.getLabel();
		if (label != null) {
			byte[] macByte = macToByteArray(label.getMac());
			System.arraycopy(macByte, 0, goodsBytes, 0, macByte.length);
		}
		try {
			System.arraycopy(goods.getName().getBytes(), 0, goodsBytes, 6, goods.getName().getBytes().length);
			System.arraycopy(goods.getAddress().getBytes(), 0, goodsBytes, 36, goods.getAddress().getBytes().length);
			System.arraycopy(goods.getSpec().getBytes(), 0, goodsBytes, 48, goods.getSpec().getBytes().length);
			System.arraycopy(goods.getScale().getBytes(), 0, goodsBytes, 54, goods.getScale().getBytes().length);
			System.arraycopy(goods.getUnit().getBytes(), 0, goodsBytes, 60, goods.getUnit().getBytes().length);
			System.arraycopy(goods.getSnid().getBytes(), 0, goodsBytes, 66, goods.getSnid().getBytes().length);
			System.arraycopy(goods.getPeriod().getBytes(), 0, goodsBytes, 72, goods.getPeriod().getBytes().length);
		}catch (Exception e) {
			log.error("字符编码转换错误", e.getMessage());
		}
		
		infoByte.writeBytes(goodsBytes);
		infoByte.writeDouble(goods.getPrice());
		return infoByte;
	}
	
	/**
	 * 生成响应Info数据帧
	 * @Title respInfoData
	 * @Description TODO()
	 * @params @param status
	 * @params @param mac
	 * @params @return
	 * @return ByteBuf
	 * @throws 
	 * @create 2018年8月24日 上午9:41:50
	 * @lastUpdate 2018年8月24日 上午9:41:50
	 */
	public static ByteBuf respInfoData(ResultStatus status, String mac) {
		ByteBuf infoByte = Unpooled.buffer(NettyConstant.RESP_INFO_LENGTH);
		byte[] macByte = macToByteArray(mac);
		infoByte.writeByte(status.value());
		infoByte.writeBytes(macByte);
		return infoByte;
	}
	
	/**
	 * 生成请求Register数据帧
	 * @Title reqRegisterData
	 * @Description TODO()
	 * @params @param gatewayName
	 * @params @param md5
	 * @params @return
	 * @return ByteBuf
	 * @throws 
	 * @create 2018年8月24日 上午9:57:13
	 * @lastUpdate 2018年8月24日 上午9:57:13
	 */
	public static ByteBuf reqRegisterData(String gatewayName, String md5) {
		ByteBuf registerByte = Unpooled.buffer(NettyConstant.REQ_REGISTER_LENGTH);
		byte[] regBytes = new byte[NettyConstant.REQ_REGISTER_LENGTH];
		try {
			System.arraycopy(gatewayName.getBytes(), 0, regBytes, 0, gatewayName.getBytes().length);
			System.arraycopy(md5.getBytes(), 0, regBytes, 8, md5.getBytes().length);
		}catch (Exception e) {

			log.error("字符编码转换错误", e.getMessage());
		}
		
		registerByte.writeBytes(regBytes);
		return registerByte;
	}

	/**
	 * mac地址字符串转换为byte数组
	 * @Title macToByteArray
	 * @Description TODO()
	 * @params @param mac
	 * @params @return
	 * @return byte[]
	 * @throws 
	 * @create 2018年8月22日 上午9:56:38
	 * @lastUpdate 2018年8月22日 上午9:56:38
	 */
	public static byte[] macToByteArray(String mac) {
		byte[] macBytes = new byte[6];
		String[] strArr = mac.split(":");

		for (int i = 0; i < strArr.length; i++) {
			int value = Integer.parseInt(strArr[i], 16);
			macBytes[i] = (byte) value;
		}
		return macBytes;
	}

	/**
	 * mac地址byte数组转换为字符串
	 * @Title ByteArrayToMac
	 * @Description TODO()
	 * @params @param macBytes
	 * @params @return
	 * @return String
	 * @throws 
	 * @create 2018年8月22日 上午9:56:55
	 * @lastUpdate 2018年8月22日 上午9:56:55
	 */
	public static String ByteArrayToMac(byte[] macBytes) {
		String value = "";
		for (int i = 0; i < macBytes.length; i++) {
			String sTemp = Integer.toHexString(0xFF & macBytes[i]).toUpperCase();
			value = value + sTemp + ":";
		}

		value = value.substring(0, value.lastIndexOf(":"));
		return value;
	}
}
