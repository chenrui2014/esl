package com.boe.esl.websocketServer;

import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.socket.struct.MessageType;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

@Slf4j
public class NetworkTimeTask extends TimerTask {

    private boolean isRun;

    private SocketChannel channel;

    public NetworkTimeTask(SocketChannel channel) {
        this.isRun = false;
        this.channel = channel;
    }

    @Override
    public void run() {
        ESLMessage networkMsg = new ESLMessage();
        ESLHeader header = new ESLHeader();
        header.setCode(HeaderType.REQ);
        header.setType(MessageType.NETWORKING);
        byte[] content = new byte[1];
        content[0] = 0x02;//停止组网
        header.setLength((byte) content.length);
        networkMsg.setContent(content);
        networkMsg.setEslHeader(header);
        ChannelFuture future = this.channel.writeAndFlush(networkMsg);
        future.addListener(future1 -> log.info("停止组网成功"));
        isRun=true;
    }

    public boolean isRun() {
        return isRun;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }
}
