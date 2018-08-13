package com.boe.esl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boe.esl.model.Goods;
import com.boe.esl.model.Label;
import com.boe.esl.service.GoodsService;
import com.boe.esl.service.LabelService;
import com.boe.esl.vo.GoodsVO;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GoodsController {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private LabelService labelService;
	
	public ResponseEntity<Map<String, Object>> getGoodsPerPage(@RequestParam(value = "page")Integer page, @RequestParam(value = "size")Integer size){
		Sort sort = new Sort(Direction.DESC, "name");
		Pageable pageable = new PageRequest(page,size,sort);
		Page<Goods> goodsPage = goodsService.findAllPaging(pageable);
		Map<String, Object> body = new HashMap<>();
		body.put("data",convertPOJOList(goodsPage.getContent()));
		body.put("number", goodsPage.getNumber());
		body.put("size", goodsPage.getSize());
		body.put("totalElements", goodsPage.getTotalElements());
		body.put("totalPages", goodsPage.getTotalPages());
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	@RequestMapping(value="/allGoods",method=RequestMethod.GET)
	public List<GoodsVO> getAllGoods(){	
		return convertPOJOList(goodsService.findAll());
	}
	private GoodsVO convertPOJO(Goods goods) {
		GoodsVO goodsVO = new GoodsVO();
		goodsVO.setName(goods.getName());
		Label label = goods.getLabel();
		if(label != null) {
			goodsVO.setLabelId(label.getId());
			goodsVO.setLabelName(label.getName());
			goodsVO.setLabelMac(label.getMac());
		}
		goodsVO.setPrice(goods.getPrice());
		goodsVO.setUnit(goods.getUnit());
		
		return goodsVO;
	}
	
	private Goods convertVO(GoodsVO goodsVO) {
		Goods goods = new Goods();
		goods.setName(goodsVO.getName());
		goods.setPrice(goodsVO.getPrice());
		goods.setUnit(goodsVO.getUnit());
		Optional<Label> labelOp = labelService.findById(goodsVO.getLabelId());
		goods.setLabel(labelOp.orElse(null));
		return goods;
	}
	
	private List<GoodsVO> convertPOJOList(List<Goods> goodsList){
		List<GoodsVO> goodsVOList = new ArrayList<GoodsVO>();
		for (Goods goods : goodsList) {
			GoodsVO goodsVO = convertPOJO(goods);
			goodsVOList.add(goodsVO);
		}
		return goodsVOList;
	}
}
