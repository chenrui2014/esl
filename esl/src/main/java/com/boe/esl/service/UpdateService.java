package com.boe.esl.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.boe.esl.model.Update;

public interface UpdateService {

	List<Update> findAll();
	List<Update> findAll(Sort sort);
	Page<Update> findAllPaging(Pageable pageable);
	Page<Update> findByLabelId(long labelId, Pageable pageable);
	List<Update> findByLabelIdNoPage(long labelId);
	Update save(Update update);
	Optional<Update> findById(long id);
}
