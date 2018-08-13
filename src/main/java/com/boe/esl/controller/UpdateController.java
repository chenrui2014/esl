package com.boe.esl.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.boe.esl.model.Goods;
import com.boe.esl.model.Label;
import com.boe.esl.model.Update;
import com.boe.esl.service.GoodsService;
import com.boe.esl.service.UpdateService;
import com.boe.esl.vo.UpdateVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UpdateController {
	@Autowired
	private UpdateService updateService;
	
	@Autowired
	private GoodsService goodsService;

	
	private UpdateVO convertPOJO(Update update) {
		UpdateVO updateVO = new UpdateVO();
		Label label = update.getLabel();
		if(label != null) {
			updateVO.setLabelId(label.getId());
			updateVO.setLabelName(label.getName());
			updateVO.setLabelMac(label.getMac());
			Goods goods = goodsService.findByLabelId(label.getId());
			if(goods != null) {
				updateVO.setGoodsId(goods.getId());
				updateVO.setGoodsName(goods.getName());
			}
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = formatter.format(update.getTime());
		updateVO.setUpdateTime(formattedDate);
		
		return updateVO;
	}
	
	private Update convertVO(UpdateVO updateVO) {
		Update update = new Update();
		update.setStatus(updateVO.getStatus());
		Timestamp ts = new Timestamp(System.currentTimeMillis()); 
		try {   
            ts = Timestamp.valueOf(updateVO.getUpdateTime());     
        } catch (Exception e) {   
            e.printStackTrace();   
        } 
		update.setTime(ts);
		return update;
	}
	
	private List<UpdateVO> convertPOJOList(List<Update> updateList){
		List<UpdateVO> updateVOList = new ArrayList<UpdateVO>();
		for (Update update : updateList) {
			UpdateVO updateVO = convertPOJO(update);
			updateVOList.add(updateVO);
		}
		return updateVOList;
	}
}
