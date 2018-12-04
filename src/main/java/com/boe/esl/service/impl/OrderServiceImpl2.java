package com.boe.esl.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.boe.esl.model.Order;
import com.boe.esl.repository.OrderRepository;
import com.boe.esl.service.OrderService2;

@Service
@Transactional
public class OrderServiceImpl2 implements OrderService2 {
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public List<Order> findAll() {

		Iterator<Order> orderIt = orderRepository.findAll().iterator();
		List<Order> orderList = new ArrayList<Order>();
		while (orderIt.hasNext()) {
			Order label = orderIt.next();
			orderList.add(label);
		}
		return orderList;
	}

	@Override
	public List<Order> findAll(Sort sort) {
		Iterator<Order> orderIt = orderRepository.findAll(sort).iterator();
		List<Order> orderList = new ArrayList<Order>();
		while (orderIt.hasNext()) {
			Order label = orderIt.next();
			orderList.add(label);
		}
		return orderList;
	}

	@Override
	public Page<Order> findPaging(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	@Override
	public Order save(Order order) {
		return orderRepository.save(order);
	}

	@Override
	public Order findById(long id) {
		return orderRepository.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {

		orderRepository.deleteById(id);
	}

	@Override
	public List<Order> findByStatus(Sort sort, String status) {
		
		return orderRepository.findByStatus(sort,status);
	}

}
