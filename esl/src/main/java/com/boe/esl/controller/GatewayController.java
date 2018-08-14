package com.boe.esl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boe.esl.model.Gateway;
import com.boe.esl.model.Label;
import com.boe.esl.service.GatewayService;
import com.boe.esl.service.LabelService;
import com.boe.esl.vo.GatewayVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GatewayController {

	@Autowired
	private GatewayService gatewayService;
	@GetMapping(value="/gateways")
	public ResponseEntity<Map<String, Object>> getGatewayPerPage(@RequestParam(value = "page")Integer page, @RequestParam(value = "size")Integer size){
		Sort sort = new Sort(Direction.DESC, "name");
		Pageable pageable = new PageRequest(page,size,sort);
		Page<Gateway> gatewayPage = gatewayService.findAllPaging(pageable);
		Map<String, Object> body = new HashMap<>();
		body.put("data",convertPOJOList(gatewayPage.getContent()));
		body.put("number", gatewayPage.getNumber());
		body.put("size", gatewayPage.getSize());
		body.put("totalElements", gatewayPage.getTotalElements());
		body.put("totalPages", gatewayPage.getTotalPages());
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	@GetMapping(value="/allGateway")
	public List<GatewayVO> getAllGateway(){
		return convertPOJOList(gatewayService.findAll());
	}
	
	private GatewayVO convertPOJO(Gateway gateway) {
		GatewayVO gatewayVO = new GatewayVO();
		gatewayVO.setKey(gateway.getKey());
		gatewayVO.setName(gateway.getName());
		gatewayVO.setOnline(gateway.isOnline());
		if(gateway.isOnline()) {
			gatewayVO.setStatus("在线");
		}else {
			gatewayVO.setStatus("离线");
		}
		
		return gatewayVO;
	}
	
	private Gateway convertVO(GatewayVO gatewayVO) {
		Gateway gateway = new Gateway();
		gateway.setKey(gatewayVO.getKey());
		gateway.setName(gatewayVO.getName());
		gateway.setOnline(gatewayVO.isOnline());
		
		return gateway;
	}
	
	private List<GatewayVO> convertPOJOList(List<Gateway> gatewayList){
		List<GatewayVO> gatewayVOList = new ArrayList<GatewayVO>();
		for (Gateway gateway : gatewayList) {
			GatewayVO gatewayVO = convertPOJO(gateway);
			gatewayVOList.add(gatewayVO);
		}
		return gatewayVOList;
	}
}
