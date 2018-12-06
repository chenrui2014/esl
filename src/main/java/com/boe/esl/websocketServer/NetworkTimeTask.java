package com.boe.esl.websocketServer;

import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.socket.struct.MessageType;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

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
        this.channel.writeAndFlush(networkMsg);
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
