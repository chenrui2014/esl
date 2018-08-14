package com.boe.esl.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.boe.esl.model.Goods;

public interface GoodsService {

	List<Goods> findAll();
	List<Goods> findAll(Sort sort);
	Page<Goods> findAllPaging(Pageable pageable);
	Goods findByLabelId(long labelId);
	Optional<Goods> findById(long id);
	Goods save(Goods goods);
}
