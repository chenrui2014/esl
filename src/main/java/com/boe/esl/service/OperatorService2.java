package com.boe.esl.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.boe.esl.model.Operator;

public interface OperatorService2 {

	List<Operator> findAll();
	List<Operator> findAll(Sort sort);
	Page<Operator> findPaging(Pageable pageable);
	Operator save(Operator operator);
	Operator findById(long id);
	void delete(Long id);
}
