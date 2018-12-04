package com.boe.esl.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.boe.esl.model.Order;

public interface OrderService2 {

	List<Order> findAll();
	List<Order> findAll(Sort sort);
	Page<Order> findPaging(Pageable pageable);
	Order save(Order operation);
	List<Order> findByStatus(Sort sort, String status);
	Order findById(long id);
	void delete(Long id);
}
