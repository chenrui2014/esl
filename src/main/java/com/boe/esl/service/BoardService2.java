package com.boe.esl.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.boe.esl.model.Board;

public interface BoardService2 {
	List<Board> findAll();
	List<Board> findALl(Sort sort);
	Page<Board> findAllPaging(Pageable pageable);
	Board save(Board board);
	Board findById(long id);
	void delete(Long id);
}
