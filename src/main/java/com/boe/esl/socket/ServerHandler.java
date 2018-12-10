package com.boe.esl.socket;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.boe.esl.controller.ControlSender;
import com.boe.esl.controller.StatusSender;
import com.boe.esl.model.*;
import com.boe.esl.service.*;
import com.boe.esl.socket.struct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.boe.esl.websocketServer.MessageEventHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Qualifier("serverHandler")
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private UpdateService updateService;

    @Autowired
    private MessageEventHandler websocketHandler;

    @Autowired
    private ControlSender controlSender;

    @Autowired
    private StatusSender statusSender;

    // 成功注册的channel
    private Map<String, SocketChannel> regChannelGroup = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ESLMessage eslMessage = (ESLMessage) msg;
        ESLHeader header = eslMessage.getEslHeader();
        System.out.println("消息类型：" + header.getType());
        if (header != null) {
            switch (header.getType()) {
                case REGISTER://注册
                    handleRegister((SocketChannel) ctx.channel(), eslMessage);
                    break;
                case NETWORKING://组网
                    break;
                case PONG://心跳
                    pong((SocketChannel) ctx.channel());
                    break;
                case NETWORK://新设备入网
                    network((SocketChannel) ctx.channel(), eslMessage);
                    break;
                case UPDATE://状态更新
                    update((SocketChannel) ctx.channel(), eslMessage);
                    break;
                case CONTROL://控制命令
                    control((SocketChannel) ctx.channel(), eslMessage);
                    break;
                case DISPLAY://显示更新
                    display((SocketChannel) ctx.channel(), eslMessage);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("\nChannel is disconnected");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("已经20秒没有收到信息！");
                log.info("向客户端发送ping消息");
                // 向客户端发送消息
                ESLMessage eslMessage = new ESLMessage();
                ESLHeader header = new ESLHeader();
                header.setCode(HeaderType.REQ);
                header.setLength((byte) 0);
                eslMessage.setEslHeader(header);
                eslMessage.setContent(new byte[0]);
                ctx.writeAndFlush(eslMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }

        }

        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.handlerRemoved(ctx);
    }

    /**
     * 处理注册类消息
     *
     * @return ESLMessage
     * @throws @create 2018年8月24日 下午1:53:18
     * @Title register
     * @Description TODO()
     * @params @param eslMessage
     * @params @return
     * @lastUpdate 2018年8月24日 下午1:53:18
     */
    private void handleRegister(SocketChannel channel, ESLMessage eslMessage) {
        byte[] keyBytes = new byte[8];
        System.arraycopy(eslMessage.getContent(), 0, keyBytes, 0, 8);
        byte[] gatewayBytes = new byte[32];
        System.arraycopy(eslMessage.getContent(), 8, gatewayBytes, 0, 32);
        String key = null;
        String gatewayName = null;
//        key = new String(keyBytes);
        key = ESLSocketUtils.ByteArrayToMac(keyBytes);
        gatewayName = new String(gatewayBytes);
        gatewayName = gatewayName.trim();
        key = key.trim();
        Gateway gateway = new Gateway();
        gateway.setName(gatewayName);
        gateway.setKey(key);
        gateway.setMac(key);
        //gateway.setOnline(true);
        Gateway gateway2 = gatewayService.findByNameAndKey(gateway);
        if (gateway2 != null) {
            gateway = gateway2;
        }

        ESLMessage respMessage = new ESLMessage();
        ESLHeader header = new ESLHeader();
        header.setCode(HeaderType.RESP);
        header.setType(MessageType.REGISTER);
        header.setLength((byte) 1);
        respMessage.setEslHeader(header);
        try {
            gatewayService.save(gateway);
            byte[] status = {ResultStatus.OK.value()};
            respMessage.setContent(status);

            regChannelGroup.put(key, channel);// 保存通道

        } catch (Exception e) {

            byte[] status = {ResultStatus.SERVER_ERROR.value()};
            respMessage.setContent(status);
        }

        channel.writeAndFlush(respMessage);
    }

    /**
     * 组网
     *
     * @param channel
     */
    private void networking(SocketChannel channel, ESLMessage eslMessage) {
//        ESLMessage networkingMsg = new ESLMessage();
//        ESLHeader header = new ESLHeader();
//        byte[] content = new byte[1];
//        content[0] = 0x01;//开始组网
//        header.setCode(HeaderType.REQ);
//        header.setType(MessageType.NETWORKING);
//        header.setLength((byte) content.length);
//        networkingMsg.setEslHeader(header);
//        networkingMsg.setContent(content);
//        channel.writeAndFlush(networkingMsg);
    }

    /**
     * 响应心跳
     * @param channel
     */
    private void pong(SocketChannel channel) {
//        byte[] gatewayBytes = new byte[8];
//        System.arraycopy(eslMessage.getContent(), 0, gatewayBytes, 0, 8);
//        String key = null;
//        key = new String(gatewayBytes);
//        key = key.trim();
//        SocketChannel ch = regChannelGroup.get(gatewayName);
        ESLMessage pongMsg = new ESLMessage();
        ESLHeader header = new ESLHeader();
        header.setCode(HeaderType.RESP);
        header.setType(MessageType.PONG);
        byte[] content = new byte[0];
        pongMsg.setContent(content);
        header.setLength((byte) content.length);
        pongMsg.setEslHeader(header);

        channel.writeAndFlush(pongMsg);
    }

    /**
     * 新设备入网
     *
     * @param channel
     * @param eslMessage
     */
    private void network(SocketChannel channel, ESLMessage eslMessage) {
        byte[] deviceTypeBytes = new byte[1];
        System.arraycopy(eslMessage.getContent(), 0, deviceTypeBytes, 0, 1);

        switch (deviceTypeBytes[0]) {
            case 0x01:
                byte[] deviceIDBytes = new byte[8];
                System.arraycopy(eslMessage.getContent(), 1, deviceIDBytes, 0, 8);
                String deviceID = null;
//                deviceID = new String(deviceIDBytes);
//                deviceID = deviceID.trim();
                deviceID = ESLSocketUtils.ByteArrayToMac(deviceIDBytes);
                Gateway gateway = new Gateway();
                gateway.setMac(deviceID);
                gateway.setKey(deviceID);
                gatewayService.save(gateway);
                regChannelGroup.put(deviceID, channel);// 保存通道
                break;
            case 0x02:
            case 0x03:
            case 0x04:
                byte[] labelIDBytes = new byte[8];
                System.arraycopy(eslMessage.getContent(), 1, labelIDBytes, 0, 8);
                String labelID = null;
//                labelID = new String(labelIDBytes);
//                labelID = labelID.trim();
                labelID = ESLSocketUtils.ByteArrayToMac(labelIDBytes);
                Label label = labelService.getLabelByCode(labelID);
                if(label != null){

                    Iterator<Map.Entry<String,SocketChannel>> it = regChannelGroup.entrySet().iterator();
                    boolean isReg=false;
                    while (it.hasNext()){
                        Map.Entry<String,SocketChannel> entry = it.next();
                        if(entry.getValue().equals(channel)){
                            try {
                                Gateway gateway1 = gatewayService.getGatewayByKey(entry.getKey());
                                if(gateway1 != null){
                                    if(label.getGateway() != null && gateway1.getKey().equals(label.getGateway().getKey())){
                                        if(LabelStatus.NETWORKING.getCode() == label.getStatus()){
                                            label.setStatus(LabelStatus.ON_LINE.getCode());//AP组网成功向CA发出新设备入网请求，CA将设备状态更新为在线
                                        }
                                        labelService.save(label);//同一网关可组网
                                        controlSender.send(label);//写入消息队列
                                        List<Label> labelList = labelService.getLabelListByGateway(gateway1.getId());
                                        boolean isFinish=true;
                                        if(labelList != null && labelList.size() > 0){
                                            for (Label label2:
                                                    labelList) {
                                                if(LabelStatus.NETWORKING.getCode() == label2.getStatus()){
                                                    isFinish=false;
                                                }
                                            }
                                        }
                                        if(isFinish){//如果该AP所有标签都完成接入，则发送停止组网消息给AP，并停止定时
                                            ESLMessage networkMsg = new ESLMessage();
                                            ESLHeader header = new ESLHeader();
                                            header.setCode(HeaderType.REQ);
                                            header.setType(MessageType.NETWORKING);
                                            byte[] content = new byte[1];
                                            content[0] = 0x02;//停止组网
                                            header.setLength((byte) content.length);
                                            networkMsg.setContent(content);
                                            networkMsg.setEslHeader(header);
                                           entry.getValue().writeAndFlush(networkMsg);
                                           //取消定时任务
                                           Timer timer = websocketHandler.getTimerMap().get(entry.getKey());
                                           if(timer != null){
                                               timer.cancel();
                                           }
                                        }

                                    }else{
                                        log.error("该设备已经与其他AP组网，请确认");
                                    }
                                }else{
                                    log.error("网关不存在或者没有注册，请先注册");
                                }
                            }catch (Exception e){
                                log.error("数据库错误",e);
                            }
                            //发送应答
                            ESLHeader header = new ESLHeader();
                            ESLMessage respMsg = new ESLMessage();
                            header.setCode(HeaderType.RESP);
                            header.setType(MessageType.NETWORK);
                            byte[] content = new byte[0];
                            respMsg.setContent(content);
                            header.setLength((byte) content.length);
                            respMsg.setEslHeader(header);
                            respMsg.setContent(content);
                            entry.getValue().writeAndFlush(respMsg);
                            isReg=true;
                            websocketHandler.toAll(label);
                            break;
                        }
                    }
                    if(!isReg){
                        //网关未注册，需要注册
                        log.error("网关没有注册，请先注册");
                    }
                }
                break;
            default:
                break;
        }

    }

    /**
     * 控制命令
     *
     * @param channel
     * @param eslMessage
     */
    private void control(SocketChannel channel, ESLMessage eslMessage) {
//        byte[] deviceTypeBytes = new byte[1];
//        System.arraycopy(eslMessage.getContent(), 0, deviceTypeBytes, 0, 1);
//
//        switch (deviceTypeBytes[0]) {
//            case 0x02:
//            case 0x03:
//            case 0x04:
//                byte[] deviceIDBytes = new byte[8];
//                System.arraycopy(eslMessage.getContent(), 1, deviceIDBytes, 0, 8);
//                String deviceID = null;
//                deviceID = new String(deviceIDBytes);
//                deviceID = deviceID.trim();
//                Label label =new Label();
//                label.setCode(deviceID);
//                websocketHandler.toAll(label);
//                break;
//        }
    }

    /**
     * 设备状态更新
     *
     * @param channel
     * @param eslMessage
     */
    private void update(SocketChannel channel, ESLMessage eslMessage) {
        byte[] deviceTypeBytes = new byte[1];
        System.arraycopy(eslMessage.getContent(), 0, deviceTypeBytes, 0, 1);

        switch (deviceTypeBytes[0]) {
            case 0x02:
            case 0x03:
            case 0x04:
                byte[] deviceIDBytes = new byte[8];
                System.arraycopy(eslMessage.getContent(), 1, deviceIDBytes, 0, 8);
                String deviceID = null;
//                deviceID = new String(deviceIDBytes);
//                deviceID = deviceID.trim();
                deviceID = ESLSocketUtils.ByteArrayToDeviceId(deviceIDBytes);
//                Label label = new Label();
//                label.setCode(deviceID);
                Label label = labelService.getLabelByCode(deviceID);
                if(label != null){
                    byte[] statusByte = new byte[1];
                    System.arraycopy(eslMessage.getContent(), 9, statusByte, 0, 1);
                    if(statusByte[0] == 0x00){
                        label.setStatus(LabelStatus.OFF_LINE.getCode());
                    }

                    if(statusByte[0] == 0x01){
                        label.setStatus(LabelStatus.ON_LINE.getCode());
                    }

                    byte[] powerBytes = new byte[1];
                    System.arraycopy(eslMessage.getContent(), 10, powerBytes, 0, 1);
                    int power;
                    power = powerBytes[0];
                    String powerStr = power+"%";
                    label.setPower(powerStr);
                    labelService.save(label);
                    statusSender.send(label);//将标签状态写入消息队列
                    int totalLength = eslMessage.getEslHeader().getLength();
                    int length = totalLength -11;
                    if(length > 3){
                        byte[] nameBytes = new byte[length-3];//丢弃0
                        byte[] shortBytes = new byte[2];
                        System.arraycopy(eslMessage.getContent(), 11, nameBytes, 0, nameBytes.length);
                        System.arraycopy(eslMessage.getContent(), 11 + nameBytes.length + 1, shortBytes, 0, 2);
                        short mNum = ESLSocketUtils.byteArray2Short(shortBytes);
                        String mName ="";
                        try {
                            mName= new String(nameBytes);
                        }catch (Exception e){
                            log.error("字符编码转换错误", e.getMessage());
                        }
                        Update update1 =updateService.getLatestUpdateByLabelCode(label.getCode());
                        if(update1 != null && label.equals(update1.getLabel())){
                            if(!mName.equals(update1.getMaterialName()) || !(mNum == update1.getMaterialNum())){
                                update1.setMaterialNum(mNum);
                                update1.setMaterialName(mName);
                                updateService.save(update1);
                            }
                        }
                    }

                    websocketHandler.toAll(label);
                }
                break;
        }
    }

    /**
     * 显示更新
     *
     * @param channel
     * @param eslMessage
     */
    private void display(SocketChannel channel, ESLMessage eslMessage) {
//        UpdateVO updateVO = new UpdateVO();
//        websocketHandler.toAll(updateVO);
    }

    public GatewayService getGatewayService() {
        return gatewayService;
    }

    public void setGatewayService(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    public MessageEventHandler getWebsocketHandler() {
        return websocketHandler;
    }

    public void setWebsocketHandler(MessageEventHandler websocketHandler) {
        this.websocketHandler = websocketHandler;
    }

    public Map<String, SocketChannel> getRegChannelGroup() {
        return regChannelGroup;
    }

    public void setRegChannelGroup(Map<String, SocketChannel> regChannelGroup) {
        this.regChannelGroup = regChannelGroup;
    }
}
