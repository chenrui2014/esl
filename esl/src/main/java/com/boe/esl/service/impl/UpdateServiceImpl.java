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

import com.boe.esl.model.Update;
import com.boe.esl.repository.UpdateRepository;
import com.boe.esl.service.UpdateService;

@Service
@Transactional
public class UpdateServiceImpl implements UpdateService {

	@Autowired
	private UpdateRepository updateRepository;
	@Override
	public List<Update> findAll() {
		Iterator<Update> updateIt = updateRepository.findAll().iterator();
		List<Update> updateList = new ArrayList<Update>();
		while (updateIt.hasNext()) {
			Update udpate = updateIt.next();
			updateList.add(udpate);
		}
		return updateList;
	}

	@Override
	public List<Update> findAll(Sort sort) {
		Iterator<Update> updateIt = updateRepository.findAll(sort).iterator();
		List<Update> updateList = new ArrayList<Update>();
		while (updateIt.hasNext()) {
			Update udpate = updateIt.next();
			updateList.add(udpate);
		}
		return updateList;
	}

	@Override
	public Page<Update> findAllPaging(Pageable pageable) {
		return updateRepository.findAll(pageable);
	}

	@Override
	public Page<Update> findByLabelId(long labelId, Pageable pageable) {
		return updateRepository.findByLabel_IdOrderById(labelId, pageable);
	}

	@Override
	public List<Update> findByLabelIdNoPage(long labelId) {
		return updateRepository.findByLabel_IdOrderById(labelId);
	}

	@Override
	public Update save(Update update) {
		return updateRepository.save(update);
	}

	@Override
	public Optional<Update> findById(long id) {
		return updateRepository.findById(id);
	}

}
