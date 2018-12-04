package com.boe.esl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boe.esl.annotation.ApiVersion;
import com.boe.esl.model.Order;
import com.boe.esl.service.OrderService;
import com.boe.esl.vo.OrderVO;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/{version}/orders")
@RestController
@Slf4j
public class OrderController {

	@Autowired
	private OrderService orderService;

	@ApiVersion(1)
	@RequestMapping(value = "/sentOrder", method = RequestMethod.GET)
	public RestResult<OrderVO> sentOrder() {
		try {
			List<Order> orders = orderService.getUnSendOrder();
			if (orders != null && orders.size() > 0) {
				return RestResultGenerator.genSuccessResult(orderService.convertEntity(orders.get(0)));
			} else {
				return RestResultGenerator.genErrorResult("没有未下发的订单");
			}
		} catch (Exception e) {

			log.error("查询订单失败",e);
			return RestResultGenerator.genErrorResult("查询订单失败");
		}

	}
	
	@ApiVersion(1)
	@GetMapping(value = "/orders")
	public PageRestResult<List<OrderVO>> getOrderPerPage(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "orderTime");
		Page<Order> orderPage = orderService.findAll(pageable);
		return RestResultGenerator.genPageResult(orderService.convertEntityList(orderPage.getContent()),
				orderPage.getNumber(), orderPage.getSize(), orderPage.getTotalElements(), orderPage.getTotalPages());
	}

	@ApiVersion(1)
	@GetMapping(value = "")
	public RestResult<List<OrderVO>> getAllOrder() {
		return RestResultGenerator.genSuccessResult(orderService.convertEntityList(orderService.findAll()));
	}
	@ApiVersion(1)
	@GetMapping(value = "/{id}")
	public RestResult<OrderVO> getOrder(@PathVariable(value = "id") Long id) {
		return RestResultGenerator
				.genSuccessResult(orderService.convertEntity(orderService.findById(id)));
	}

	@ApiVersion(1)
	@PostMapping(value = "")
	public RestResult<OrderVO> addOrder(@RequestBody OrderVO orderVO) {
		return RestResultGenerator
				.genSuccessResult(orderService.convertEntity(orderService.save(orderService.convertVO(orderVO))));
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}")
	public RestResult<OrderVO> updateOrder(@PathVariable("id") Long id, @RequestBody OrderVO orderVO) {
		orderVO.setId(id);
		return RestResultGenerator
				.genSuccessResult(orderService.convertEntity(orderService.save(orderService.convertVO(orderVO))));
	}

	@SuppressWarnings("rawtypes")
	@ApiVersion(1)
	@DeleteMapping(value = "/{id}")
	public RestResult removeOrder(@PathVariable("id") Long id) {
		orderService.del(id);
		return RestResultGenerator.genSuccessResult();
	}
}
