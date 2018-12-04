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

import com.boe.esl.model.Operation;
import com.boe.esl.repository.OperationRepository;
import com.boe.esl.service.OperationService2;

@Service
@Transactional
public class OperationServiceImpl2 implements OperationService2 {
	
	@Autowired
	private OperationRepository operationRepository;

	@Override
	public List<Operation> findAll() {
		Iterator<Operation> operationIt = operationRepository.findAll().iterator();
		List<Operation> operationList = new ArrayList<Operation>();
		while (operationIt.hasNext()) {
			Operation label = operationIt.next();
			operationList.add(label);
		}
		return operationList;
	}

	@Override
	public List<Operation> findAll(Sort sort) {
		Iterator<Operation> operationIt = operationRepository.findAll(sort).iterator();
		List<Operation> operationList = new ArrayList<Operation>();
		while (operationIt.hasNext()) {
			Operation label = operationIt.next();
			operationList.add(label);
		}
		return operationList;
	}

	@Override
	public Page<Operation> findPaging(Pageable pageable) {
		return operationRepository.findAll(pageable);
	}

	@Override
	public Operation save(Operation operation) {
		return operationRepository.save(operation);
	}

	@Override
	public Operation findById(long id) {
		return operationRepository.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		operationRepository.deleteById(id);
	}

}
