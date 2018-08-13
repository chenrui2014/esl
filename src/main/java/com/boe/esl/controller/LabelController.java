package com.boe.esl.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.boe.esl.model.Gateway;
import com.boe.esl.model.Label;
import com.boe.esl.service.LabelService;
import com.boe.esl.vo.LabelVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LabelController {

	@Autowired
	private LabelService labelService;
	
	private LabelVO convertPOJO(Label label) {
		LabelVO labelVO = new LabelVO();
		labelVO.setName(label.getName());
		labelVO.setMac(label.getMac());
		labelVO.setShelf(label.getShelf());
		labelVO.setStatus(label.getStatus());
		Gateway gateway = label.getGateway();
		if(gateway != null) {
			labelVO.setGatewayId(gateway.getId());
			labelVO.setGatewayName(gateway.getName());
		}
		return labelVO;
	}
	
	private Label convertVO(LabelVO labelVO) {
		Label label = new Label();
		label.setMac(labelVO.getMac());
		label.setName(labelVO.getName());
		label.setShelf(labelVO.getShelf());
		label.setStatus(labelVO.getStatus());
		return label;
	}
	
	private List<LabelVO> convertPOJOList(List<Label> labelList){
		List<LabelVO> labelVOList = new ArrayList<LabelVO>();
		for (Label label : labelList) {
			LabelVO labelVO = convertPOJO(label);
			labelVOList.add(labelVO);
		}
		return labelVOList;
	}
}
