package com.boe.esl.socket;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.boe.esl.model.ControlMessage;
import com.boe.esl.vo.UpdateVO;
import org.junit.Test;
import org.springframework.boot.origin.SystemEnvironmentOrigin;

import com.boe.esl.model.Goods;
import com.boe.esl.model.Label;
import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ESLSocketUtilsTest {
	
	@Test
	public void testmacToByteArray() {
		String mac = "30:9C:23:BF:21:6D";
		byte[] macByte = ESLSocketUtils.macToByteArray(mac);
		byte[] result = {(byte)0x30,(byte)0x9C,(byte)0x23,(byte)0xBF,(byte)0x21,(byte)0x6D};
		assertArrayEquals(macByte, result);
	}
	
	@Test
	public void testByteArrayToMac() {
		byte[] macByte = {(byte)0x30,(byte)0x9C,(byte)0x23,(byte)0xBF,(byte)0x21,(byte)0x6D};
		String mac = ESLSocketUtils.ByteArrayToMac(macByte);
		String result = "30:9C:23:BF:21:6D";
		assertTrue(mac.equals(result));
	}
	
	@Test
	public void testByteArray() {
		String name = "电脑";
		byte[] nameByte = new byte[30];
		System.arraycopy(name.getBytes(), 0, nameByte, 0, name.getBytes().length);
		ByteBuf buf = Unpooled.buffer(50);
		buf.writeBytes(nameByte);
		assertTrue(buf.writableBytes() == 20);
	}
	
	@Test
	public void testESLMessage() {
		ESLMessage esl = new ESLMessage();
		assertTrue(esl.getContent() == null);
	}
	
	@Test
	public void testMap() {
		ESLMessage eslMessage = new ESLMessage();
		ESLMessage eslMessage2 = new ESLMessage();
		ESLHeader header = new ESLHeader();
		eslMessage2.setEslHeader(header);
		Map<String, ESLMessage> map = new ConcurrentHashMap<>();
		map.put("esl", eslMessage);
		map.put("esl", eslMessage2);
		assertTrue(map.get("esl").equals(eslMessage2));
	}

	@Test
	public void testConvertLabelToByte() {
		Goods goods = new Goods();
		Label label = new Label();
		label.setMac("30:9C:23:BF:21:6D");
		goods.setLabel(label);
		goods.setName("电脑");
		ByteBuf buf =ESLSocketUtils.convertGoodsToByte(goods);
		assertTrue(buf.readableBytes() == NettyConstant.REQ_INFO_LENGTH);
	}

	@Test
	public void testCreateUpdateContent(){

		UpdateVO updateVO = new UpdateVO();
		updateVO.setSid("A901000005");
		updateVO.setMaterialName("A仓库 香蕉");
		updateVO.setMaterialNum((short)10);
		ByteBuf buf = ESLSocketUtils.createUpdateContent(updateVO);
		assertTrue(buf.readableBytes() < 255);
	}

	@Test
	public void testShortToByteArray(){
		byte[] shortByte = ESLSocketUtils.shortToByteArray((short)10);
		assertTrue(shortByte[0] == 0x0A);
	}
}
