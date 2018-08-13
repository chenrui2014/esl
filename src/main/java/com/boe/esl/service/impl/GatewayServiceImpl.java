package com.boe.esl.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.boe.esl.model.Gateway;
import com.boe.esl.repository.GatewayRepository;
import com.boe.esl.service.GatewayService;

@Service
@Transactional
public class GatewayServiceImpl implements GatewayService {

	@Autowired
	private GatewayRepository gatewayRepository;
	@Override
	public List<Gateway> findAll() {
		Iterator<Gateway> gatewayIt = gatewayRepository.findAll().iterator();
		List<Gateway> gatewayList = new ArrayList<Gateway>();
		while (gatewayIt.hasNext()) {
			Gateway gateway = gatewayIt.next();
			gatewayList.add(gateway);
		}
		return gatewayList;
	}

	@Override
	public List<Gateway> findALl(Sort sort) {
		Iterator<Gateway> gatewayIt = gatewayRepository.findAll(sort).iterator();
		List<Gateway> gatewayList = new ArrayList<Gateway>();
		while (gatewayIt.hasNext()) {
			Gateway income = gatewayIt.next();
			gatewayList.add(income);
		}
		return gatewayList;
	}

	@Override
	public Page<Gateway> findAllPaging(Pageable pageable) {
		return gatewayRepository.findAll(pageable);
	}

	@Override
	public Gateway save(Gateway gateway) {
		return gatewayRepository.save(gateway);
	}

	@Override
	public Optional<Gateway> findById(long id) {
		return gatewayRepository.findById(id);
	}

}
