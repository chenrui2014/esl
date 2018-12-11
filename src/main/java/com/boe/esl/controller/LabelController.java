package com.boe.esl.controller;

import java.sql.Timestamp;
import java.util.*;

import com.boe.esl.model.*;
import com.boe.esl.service.GatewayService;
import com.boe.esl.service.UpdateService;
import com.boe.esl.socket.ESLSocketUtils;
import com.boe.esl.socket.ServerHandler;
import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.socket.struct.MessageType;
import com.boe.esl.vo.UpdateVO;
import com.boe.esl.websocketServer.NetworkTimeTask;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boe.esl.annotation.ApiVersion;
import com.boe.esl.service.LabelService;
import com.boe.esl.vo.LabelVO;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/{version}/labels")
@RestController
@Slf4j
public class LabelController {

	@Autowired
	private LabelService labelService;

	@Autowired
	private GatewayService gatewayService;

	@Autowired
	private UpdateService updateService;

	@Autowired
	@Qualifier("serverHandler")
	private ServerHandler serverHandler;

	private Map<String, Timer> timerMap;

	@Value("${esl.network-timeout}")
	private int networkTimeout;//组网超时时间

	@Autowired
	private UpdateSender updateSender;

	@Autowired
	private WareHouseSender wareHouseSender;
	@Autowired
	private ControlSender controlSender;

	@ApiVersion(1)
	@GetMapping(value = "perpage")
	public PageRestResult<List<LabelVO>> getLabelPerPage(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "name");
		Page<Label> labelPage = labelService.findAll(pageable);
		return RestResultGenerator.genPageResult(labelService.convertEntityList(labelPage.getContent()),
				labelPage.getNumber(), labelPage.getSize(), labelPage.getTotalElements(), labelPage.getTotalPages());
	}

	@ApiVersion(1)
	@GetMapping(value = "")
	public RestResult<List<LabelVO>> getAllLabel() {
		return RestResultGenerator.genSuccessResult(labelService.convertEntityList(labelService.findAll()));
	}
	
	@ApiVersion(1)
	@GetMapping(value = "/{id}")
	public RestResult<LabelVO> getLabel(@PathVariable("id") long id) {
		return RestResultGenerator.genSuccessResult(labelService.convertEntity(labelService.findById(id)));
	}

	@ApiVersion(1)
	@PostMapping(value = "")
	public RestResult<LabelVO> addLabel(@RequestBody LabelVO labelVO) {
		return RestResultGenerator
				.genSuccessResult(labelService.convertEntity(labelService.save(labelService.convertVO(labelVO))));
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}")
	public RestResult<LabelVO> updateLabel(@PathVariable("id") long id, @RequestBody LabelVO labelVO) {
		labelVO.setId(id);
		return RestResultGenerator
				.genSuccessResult(labelService.convertEntity(labelService.save(labelService.convertVO(labelVO))));
	}

	@SuppressWarnings("rawtypes")
	@ApiVersion(1)
	@DeleteMapping(value = "/{id}")
	public RestResult removeLabel(@PathVariable("id") Long id) {
		labelService.del(id);
		return RestResultGenerator.genSuccessResult();
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}/network")
	public void network(@PathVariable("id") long id, @RequestBody List<LinkedHashMap<String, Object>> msgList){

		if (msgList != null) {

			msgList.forEach(msg -> {
				NetworkMessage networkMessage = new NetworkMessage();

				if (msg.get("gatewayMac") != null) {
					String mac = msg.get("gatewayMac").toString();
					networkMessage.setGatewayMac(mac);
				}
				if (msg.get("labelIDList") != null) {
					List<String> idList = (List<String>) msg.get("labelIDList");
					networkMessage.setLabelIDList(idList);
				}
				Gateway gateway = gatewayService.getGatewayByKey(networkMessage.getGatewayMac());
				if (gateway == null) {
					List<Label> labelList = new ArrayList<>();
					networkMessage.getLabelIDList().forEach(code -> {
						Label label = labelService.getLabelByCode(code);
						if (label != null) {
							label.setGateway(gateway);
							label.setStatus(LabelStatus.NETWORKING.getCode());
							labelList.add(label);
						}
					});
					//批量插入
					labelService.save(labelList);
				}
				Map<String, SocketChannel> regChannelGroup = serverHandler.getRegChannelGroup();
				SocketChannel ch = regChannelGroup.get(networkMessage.getGatewayMac());
				ESLMessage networkMsg = new ESLMessage();
				ESLHeader header = new ESLHeader();
				header.setCode(HeaderType.REQ);
				header.setType(MessageType.NETWORKING);
				byte[] content = new byte[1];
				content[0] = 0x01;//开始组网
				header.setLength((byte) content.length);
				networkMsg.setContent(content);
				networkMsg.setEslHeader(header);
				if (ch != null) {
					ChannelFuture future = ch.writeAndFlush(networkMsg);
					future.addListener(future1 -> {

					});
				} else {
					log.error("没有注册的注册信息");
				}

				Timer timer = new Timer();//每个网关一个定时器
				timer.schedule(new NetworkTimeTask(ch), networkTimeout);
				timerMap.put(networkMessage.getGatewayMac(), timer);
			});
		}
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}/display")
	public void update(@PathVariable("id") long id, @RequestBody UpdateVO updatevo){

		Label label = labelService.findById(id);
		Gateway gateway = null;
		if (label != null) {
			Update update = new Update();
			update.setLabel(label);
			update.setMaterialName(updatevo.getMaterialName());
			update.setMaterialNum(updatevo.getMaterialNum());
			update.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			updateService.save(update);
			gateway = label.getGateway();
			if (gateway != null) {
				Map<String, SocketChannel> regChannelGroup = serverHandler.getRegChannelGroup();
				SocketChannel ch = regChannelGroup.get(gateway.getKey());
				ESLMessage controlMsg = new ESLMessage();
				ESLHeader header = new ESLHeader();
				header.setCode(HeaderType.REQ);
				header.setType(MessageType.DISPLAY);
				byte[] content = ESLSocketUtils.createUpdateContent(updatevo).array();
				controlMsg.setContent(content);
				header.setLength((byte) content.length);
				controlMsg.setEslHeader(header);
				if (ch != null) {
					ChannelFuture future = ch.writeAndFlush(controlMsg);
					future.addListener(future1 -> {
						if(future1.isSuccess()){
							Update update1 = updateService.getLatestUpdateByLabelCode(label.getCode());
							UpdateVO updateVO = updateService.convertEntity(update1);
							updateSender.send(updateVO);
						}
						//client.sendEvent("sendControl", "更新标签内容成功");
					});
				}

			} else {
				//client.sendEvent("sendControl", "该标签没有入网");
			}
		} else {
			//client.sendEvent("sendControl", "没有找到对应的标签");
		}
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}/control")
	public void control(@PathVariable("id") long id, @RequestBody ControlMessage controlMessage){
		Label label = labelService.findById(id);
		Gateway gateway = null;
		if (label != null) {
			gateway = label.getGateway();
			if (gateway != null) {
				Map<String, SocketChannel> regChannelGroup = serverHandler.getRegChannelGroup();
				SocketChannel ch = regChannelGroup.get(gateway.getKey());
				ESLMessage controlMsg = new ESLMessage();
				ESLHeader header = new ESLHeader();
				header.setCode(HeaderType.REQ);
				header.setType(MessageType.CONTROL);
				byte[] content = ESLSocketUtils.createControlContent(controlMessage).array();
				header.setLength((byte) content.length);
				controlMsg.setEslHeader(header);
				controlMsg.setContent(content);
				if (ch != null) {
					ChannelFuture future = ch.writeAndFlush(controlMsg);
					future.addListener(future1 -> {

						if (future.isSuccess()) {
							label.setOptType(controlMessage.getOptType());
							labelService.save(label);
							wareHouseSender.send(label);
							//client.sendEvent("sendControl", "发送控制指令成功");
						}

					});
				}

			} else {
				//client.sendEvent("sendControl", "该标签没有入网");
			}
		} else {
			//client.sendEvent("sendControl", "没有找到对应的标签");
		}
	}

	@GetMapping(value = "/send")
	public void send(){
		//Label label = labelService.findById(id);
		Label label = new Label();
		label.setCode("adfsadf");
		controlSender.send(label);
	}
}
