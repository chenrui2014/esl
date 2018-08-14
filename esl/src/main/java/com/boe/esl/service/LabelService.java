package com.boe.esl.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.boe.esl.model.Label;

public interface LabelService {

	List<Label> findAll();
	List<Label> findAll(Sort sort);
	Page<Label> findAllPaging(Pageable pageable);
	Page<Label> findByGatewayId(long gatewayId, Pageable pageable);
	List<Label> findByGatewayIdNoPage(long gatewayId);
	Label save(Label label);
	Optional<Label> findById(long labelId);
}
