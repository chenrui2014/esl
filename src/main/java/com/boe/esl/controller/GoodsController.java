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
import com.boe.esl.model.Goods;
import com.boe.esl.service.GoodsService;
import com.boe.esl.socket.ServerHandler;
import com.boe.esl.vo.GoodsVO;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/{version}/goods")
@RestController
@Slf4j
public class GoodsController {

	@Autowired
	private GoodsService goodsService;

	@Autowired
	@Qualifier("serverHandler")
	private ServerHandler serverHandler;

	@ApiVersion(1)
	@GetMapping(value = "/goods")
	public PageRestResult<List<GoodsVO>> getGoodsPerPage(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "name");
		Page<Goods> goodsPage = goodsService.findAll(pageable);
		return RestResultGenerator.genPageResult(goodsService.convertEntityList(goodsPage.getContent()),
				goodsPage.getNumber(), goodsPage.getSize(), goodsPage.getTotalElements(), goodsPage.getTotalPages());
	}

	@ApiVersion(1)
	@GetMapping(value = "")
	public RestResult<List<GoodsVO>> getAllGoods() {
		return RestResultGenerator.genSuccessResult(goodsService.convertEntityList(goodsService.findAll()));
	}
	
	@ApiVersion(1)
	@GetMapping(value = "/{id}")
	public RestResult<GoodsVO> getGoods(@PathVariable("id") long id) {
		return RestResultGenerator.genSuccessResult(goodsService.convertEntity(goodsService.findById(id)));
	}

	@ApiVersion(1)
	@PostMapping(value = "")
	public RestResult<GoodsVO> addGoods(@RequestBody GoodsVO goodsVO) {
		return RestResultGenerator
				.genSuccessResult(goodsService.convertEntity(goodsService.save(goodsService.convertVO(goodsVO))));
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}")
	public RestResult<GoodsVO> updateGoods(@PathVariable("id") long id, @RequestBody GoodsVO goodsVO) {
		goodsVO.setId(id);
		return RestResultGenerator
				.genSuccessResult(goodsService.convertEntity(goodsService.save(goodsService.convertVO(goodsVO))));
	}

	@ApiVersion(1)
	@SuppressWarnings("rawtypes")
	@DeleteMapping(value = "/{id}")
	public RestResult removeGoods(@PathVariable("id") Long id) {
			goodsService.del(id);
		return RestResultGenerator.genSuccessResult();
	}
}
