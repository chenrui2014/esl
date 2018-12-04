package com.boe.esl.socket;

import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.socket.struct.MessageType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ESLMessageDecoder extends LengthFieldBasedFrameDecoder {

	public ESLMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);

		if (frame == null) {
			return null;
		}
		byte headType = frame.getByte(0);
		byte version = frame.getByte(1);
		byte type = frame.getByte(2);
		byte pading = frame.getByte(3);
		byte length = frame.getByte(4);
		byte[] content = new byte[length];
		frame.getBytes(5, content);
//		byte[] name = new byte[8];
//		System.arraycopy(content, 0, name, 0, 8);
//		String nameString = new String(name);
//		System.out.println(nameString);
//		byte[] md5 = new byte[32];
//		System.arraycopy(content, 8, md5, 0, 32);
//		String md5String = new String(md5);
//		System.out.println(md5String);
		// if(headType == HeaderType.REQ.value() || headType == HeaderType.RESP.value()
		// ) {
		ESLMessage message = new ESLMessage();
		ESLHeader header = new ESLHeader();
		switch (headType) {
		case 0x55:
			header.setCode(HeaderType.REQ);
			break;
		case (byte) 0x99:
			header.setCode(HeaderType.RESP);
			break;
		default:
			break;
		}
		header.setVersion(version);
		switch (type) {
		case 0x01:
			header.setType(MessageType.REGISTER);
			break;
		case 0x02:
			header.setType(MessageType.AWAKE);
			break;
		case 0x03:
			header.setType(MessageType.LOGIN);
			break;
		case 0x04:
			header.setType(MessageType.UPDATE);
			break;
		case 0x05:
			header.setType(MessageType.CANCEL);
			break;
		case 0x06:
			header.setType(MessageType.DONE);
			break;
		case 0x08:
			header.setType(MessageType.INFO);
			break;
		case 0x09:
			header.setType(MessageType.PING);
			break;
		case 0x0a:
			header.setType(MessageType.PONG);
			break;
		default:
			break;
		}
		header.setPading(pading);
		header.setLength(length);
		message.setEslHeader(header);
		message.setContent(content);
		return message;
	}

//	@Override
//	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//
//		if (in.readableBytes() >= BASE_LENGTH) {
//			byte headType = in.readByte();
//			// if(headType == HeaderType.REQ.value() || headType == HeaderType.RESP.value()
//			// ) {
//			ESLMessage message = new ESLMessage();
//			ESLHeader header = new ESLHeader();
//			switch (headType) {
//			case 0x55:
//				header.setCode(HeaderType.REQ);
//				break;
//			case (byte) 0x99:
//				header.setCode(HeaderType.RESP);
//				break;
//			default:
//				break;
//			}
//			header.setVersion(in.readByte());
//			switch (in.readByte()) {
//			case 0x01:
//				header.setType(MessageType.RESP_REGISTER);
//				break;
//			case 0x02:
//				header.setType(MessageType.REQ_AWAKE);
//				break;
//			case 0x03:
//				header.setType(MessageType.REQ_INFO);
//				break;
//			case 0x04:
//				header.setType(MessageType.REQ_UPDATE);
//				break;
//			case 0x05:
//				header.setType(MessageType.REQ_CANCEL);
//				break;
//			case 0x06:
//				header.setType(MessageType.RESP_DONE);
//				break;
//			default:
//				break;
//			}
//			header.setPading(in.readByte());
//			header.setLength(in.readByte());
//			message.setEslHeader(header);
//			message.setContent(in.readBytes(header.getLength()).array());
//			out.add(message);
//			// }
//
//		}
//
//	}

}
