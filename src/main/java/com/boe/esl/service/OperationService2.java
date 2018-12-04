package com.boe.esl.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.boe.esl.model.Operation;

public interface OperationService2 {

	List<Operation> findAll();
	List<Operation> findAll(Sort sort);
	Page<Operation> findPaging(Pageable pageable);
	Operation save(Operation operation);
	Operation findById(long id);
	void delete(Long id);
}
