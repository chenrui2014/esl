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

import com.boe.esl.model.Board;
import com.boe.esl.model.Board;
import com.boe.esl.model.Board;
import com.boe.esl.repository.BoardRepository;
import com.boe.esl.service.BoardService2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class BoardServiceImpl2 implements BoardService2 {
	
	@Autowired
	private BoardRepository boardRepository;

	@Override
	public List<Board> findAll() {
		Iterator<Board> boardIt = boardRepository.findAll().iterator();
		List<Board> boardList = new ArrayList<Board>();
		while (boardIt.hasNext()) {
			Board board = boardIt.next();
			boardList.add(board);
		}
		return boardList;
	}

	@Override
	public List<Board> findALl(Sort sort) {
		Iterator<Board> boardIt = boardRepository.findAll(sort).iterator();
		List<Board> boardList = new ArrayList<Board>();
		while (boardIt.hasNext()) {
			Board board = boardIt.next();
			boardList.add(board);
		}
		return boardList;
	}

	@Override
	public Page<Board> findAllPaging(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	@Override
	public Board save(Board board) {
		return boardRepository.save(board);
	}

	@Override
	public Board findById(long id) {
		return boardRepository.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {

		boardRepository.deleteById(id);
	}
}
