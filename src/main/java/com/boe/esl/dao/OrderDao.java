package com.boe.esl.dao;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.boe.esl.model.Order;

public interface OrderDao extends BaseDao<Order, Long> {

	List<Order> findByStatus(Sort sort,String status);
}
