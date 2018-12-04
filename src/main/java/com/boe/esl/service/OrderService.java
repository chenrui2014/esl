package com.boe.esl.service;

import java.util.List;

import com.boe.esl.model.Order;
import com.boe.esl.vo.OrderVO;

public interface OrderService extends BaseService<Order, Long, OrderVO> {
	
	List<Order> getUnSendOrder();
}
