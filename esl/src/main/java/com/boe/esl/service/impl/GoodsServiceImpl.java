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

import com.boe.esl.model.Goods;
import com.boe.esl.repository.GoodsRepository;
import com.boe.esl.service.GoodsService;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsRepository goodsRepository;
	@Override
	public List<Goods> findAll() {
		Iterator<Goods> goodsIt = goodsRepository.findAll().iterator();
		List<Goods> goodsList = new ArrayList<Goods>();
		while (goodsIt.hasNext()) {
			Goods goods = goodsIt.next();
			goodsList.add(goods);
		}
		return goodsList;
	}

	@Override
	public List<Goods> findAll(Sort sort) {
		Iterator<Goods> goodsIt = goodsRepository.findAll(sort).iterator();
		List<Goods> goodsList = new ArrayList<Goods>();
		while (goodsIt.hasNext()) {
			Goods goods = goodsIt.next();
			goodsList.add(goods);
		}
		return goodsList;
	}

	@Override
	public Page<Goods> findAllPaging(Pageable pageable) {
		return goodsRepository.findAll(pageable);
	}

	@Override
	public Goods findByLabelId(long labelId) {
		return goodsRepository.findByLabel_IdOrderByNameDesc(labelId);
	}

	@Override
	public Goods save(Goods goods) {
		return goodsRepository.save(goods);
	}

	@Override
	public Optional<Goods> findById(long id) {
		return goodsRepository.findById(id);
	}

}
