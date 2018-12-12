package com.boe.esl.controller;

import java.util.*;

import javax.websocket.server.PathParam;

import com.boe.esl.model.Label;
import com.boe.esl.model.LabelStatus;
import com.boe.esl.model.NetworkMessage;
import com.boe.esl.service.LabelService;
import com.boe.esl.socket.ServerHandler;
import com.boe.esl.socket.struct.ESLHeader;
import com.boe.esl.socket.struct.ESLMessage;
import com.boe.esl.socket.struct.HeaderType;
import com.boe.esl.socket.struct.MessageType;
import com.boe.esl.websocketServer.NetworkTimeTask;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
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
import com.boe.esl.model.Gateway;
import com.boe.esl.service.GatewayService;
import com.boe.esl.vo.GatewayVO;

//import graphql.ExecutionResult;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/{version}/gateways")
@RestController
@Slf4j
public class GatewayController {

	@Autowired
	private GatewayService gatewayService;

	@Autowired
	private LabelService labelService;

	@Autowired
	@Qualifier("serverHandler")
	private ServerHandler serverHandler;

	private Map<String, Timer> timerMap = new HashMap<>();

	@Value("${esl.network-timeout}")
	private int networkTimeout;//组网超时时间

//	private GraphqlService graphqlService;

//	@PostMapping(value = "/graphql/gateway")
//    public ResponseEntity<Object> allItems(@RequestBody String query) {
//		log.info("查询:"+ query);
//        ExecutionResult result = graphqlService.getGraphQL().execute(query);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
	@ApiVersion(1)
	@GetMapping(value = "perpage")
	public PageRestResult<List<GatewayVO>> getGatewayPerPage(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "name");
		Page<Gateway> gatewayPage = gatewayService.findAll(pageable);
		return RestResultGenerator.genPageResult(gatewayService.convertEntityList(gatewayPage.getContent()),
				gatewayPage.getNumber(), gatewayPage.getSize(), gatewayPage.getTotalElements(),
				gatewayPage.getTotalPages());
	}

	@ApiVersion(1)
	@GetMapping(value = "")
	public RestResult<List<GatewayVO>> getAllGateway() {
		return RestResultGenerator.genSuccessResult(gatewayService.convertEntityList(gatewayService.findAll()));
	}
	
	@ApiVersion(1)
	@GetMapping(value = "/{id}")
	public RestResult<GatewayVO> getGateway(@PathVariable("id") long id) {
		return RestResultGenerator.genSuccessResult(gatewayService.convertEntity(gatewayService.findById(id)));
	}

	@ApiVersion(1)
	@PostMapping(value = "")
	public RestResult<GatewayVO> addGateway( @RequestBody GatewayVO gatewayVO) {

		Gateway gateway = null;
		try{
			gateway = gatewayService.save(gatewayService.convertVO(gatewayVO));
			return RestResultGenerator.genSuccessResult(
					gatewayService.convertEntity(gateway));
		}catch (Exception e){
			return RestResultGenerator.genErrorResult(ResultEnum.DATA_ERROR);
		}
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}")
	public RestResult<GatewayVO> updateGateway(@PathVariable("id") Long id, @RequestBody GatewayVO gatewayVO) {
		gatewayVO.setId(id);
		return RestResultGenerator.genSuccessResult(
				gatewayService.convertEntity(gatewayService.save(gatewayService.convertVO(gatewayVO))));
	}

	@ApiVersion(1)
	@SuppressWarnings("rawtypes")
	@DeleteMapping(value = "/{id}")
	public RestResult removeGateway(@PathVariable("id") Long id) {
		gatewayService.del(id);
		return RestResultGenerator.genSuccessResult();
	}

	@ApiVersion(1)
	@PutMapping(value = "/network")
	public void network(@RequestBody List<LinkedHashMap<String, Object>> msgList){

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
						if(future1.isSuccess()) log.info("成功发起组网");
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
}
