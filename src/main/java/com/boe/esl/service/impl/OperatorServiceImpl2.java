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

import com.boe.esl.model.Operator;
import com.boe.esl.repository.OperatorRepository;
import com.boe.esl.service.OperatorService2;

@Service
@Transactional
public class OperatorServiceImpl2 implements OperatorService2 {
	
	@Autowired
	private OperatorRepository operatorRepository;

	@Override
	public Operator findById(long id) {
		return operatorRepository.findById(id).orElse(null);
	}

	@Override
	public List<Operator> findAll() {
		Iterator<Operator> operatorIt = operatorRepository.findAll().iterator();
		List<Operator> operatorList = new ArrayList<Operator>();
		while (operatorIt.hasNext()) {
			Operator label = operatorIt.next();
			operatorList.add(label);
		}
		return operatorList;
	}

	@Override
	public List<Operator> findAll(Sort sort) {
		Iterator<Operator> operatorIt = operatorRepository.findAll(sort).iterator();
		List<Operator> operatorList = new ArrayList<Operator>();
		while (operatorIt.hasNext()) {
			Operator label = operatorIt.next();
			operatorList.add(label);
		}
		return operatorList;
	}

	@Override
	public Page<Operator> findPaging(Pageable pageable) {
		return operatorRepository.findAll(pageable);
	}

	@Override
	public Operator save(Operator operator) {
		return operatorRepository.save(operator);
	}

	@Override
	public void delete(Long id) {

		operatorRepository.deleteById(id);
	}

}
