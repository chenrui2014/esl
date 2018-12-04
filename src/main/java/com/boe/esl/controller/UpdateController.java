package com.boe.esl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.boe.esl.model.Operator;
import com.boe.esl.model.Update;
import com.boe.esl.service.OperatorService;
import com.boe.esl.service.UpdateService;
import com.boe.esl.socket.ServerHandler;
import com.boe.esl.vo.UpdateVO;
import com.boe.esl.websocketServer.MessageEventHandler;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/{version}/updates")
@RestController
@Slf4j
public class UpdateController {
	@Autowired
	private UpdateService updateService;

	@Autowired
	private OperatorService operatorService;

	@Autowired
	private MessageEventHandler mEventHandler;

	@Autowired
	@Qualifier("serverHandler")
	private ServerHandler serverHandler;

	@RequestMapping("/send")
	public void sendMsg() {
		log.info("开始推送消息");
		// Message message = new Message("hello");
//		Update update = updateService.findById(4l);
//		mEventHandler.toAll(converter.converterUpdatetoVO(update));
	}

	@RequestMapping("/sendBoard")
	public void sendBoard() {
		log.info("开始推送面板消息");
//		Message message = new Message();
//		message.setBody("hello");
		Operator operator = operatorService.findById(1l);
		mEventHandler.toAllBoard(operatorService.convertEntity(operator));
	}

	public void update() {

	}

	@ApiVersion(1)
	@GetMapping(value = "/updates")
	public PageRestResult<List<UpdateVO>> getUpdatePerPage(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "id");
		Page<Update> updatePage = updateService.findAll(pageable);
		return RestResultGenerator.genPageResult(updateService.convertEntityList(updatePage.getContent()), 
				updatePage.getNumber(), updatePage.getSize(), updatePage.getTotalElements(), updatePage.getTotalPages());
	}

	@ApiVersion(1)
	@GetMapping(value = "")
	public RestResult<List<UpdateVO>> getAllUpdate() {
		return RestResultGenerator.genSuccessResult(updateService.convertEntityList(updateService.findAll()));
	}
	
	@ApiVersion(1)
	@GetMapping(value = "/{id}")
	public RestResult<UpdateVO> getUpdate(@PathVariable(value = "id") Long id) {
		return RestResultGenerator
				.genSuccessResult(updateService.convertEntity(updateService.findById(id)));
	}

	@ApiVersion(1)
	@PostMapping(value = "")
	public RestResult<UpdateVO> addUpdate(@RequestBody UpdateVO updateVO) {
		return RestResultGenerator
				.genSuccessResult(updateService.convertEntity(updateService.save(updateService.convertVO(updateVO))));
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}")
	public RestResult<UpdateVO> updateUpdate(@PathVariable(value = "id") Long id, @RequestBody UpdateVO updateVO) {
		updateVO.setId(id);
		return RestResultGenerator
				.genSuccessResult(updateService.convertEntity(updateService.save(updateService.convertVO(updateVO))));
	}

	@SuppressWarnings("rawtypes")
	@ApiVersion(1)
	@DeleteMapping(value = "/{id}")
	public RestResult removeUpdate(@PathVariable("id") Long id) {
		updateService.del(id);

		return RestResultGenerator.genSuccessResult();
	}

//	@MessageMapping("/sendMessage")
//	@SendTo("/topic/public")
//	public Message sendMessage(Message message) {
//		return message;
//	}
}
