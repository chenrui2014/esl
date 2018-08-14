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

import com.boe.esl.model.Label;
import com.boe.esl.repository.LabelRepository;
import com.boe.esl.service.LabelService;

@Service
@Transactional
public class LabelServiceImpl implements LabelService {

	@Autowired
	private LabelRepository labelRepository;
	@Override
	public List<Label> findAll() {
		Iterator<Label> labelIt = labelRepository.findAll().iterator();
		List<Label> labelList = new ArrayList<Label>();
		while (labelIt.hasNext()) {
			Label label = labelIt.next();
			labelList.add(label);
		}
		return labelList;
	}

	@Override
	public List<Label> findAll(Sort sort) {
		Iterator<Label> labelIt = labelRepository.findAll(sort).iterator();
		List<Label> labelList = new ArrayList<Label>();
		while (labelIt.hasNext()) {
			Label label = labelIt.next();
			labelList.add(label);
		}
		return labelList;
	}

	@Override
	public Page<Label> findAllPaging(Pageable pageable) {
		return labelRepository.findAll(pageable);
	}

	@Override
	public Page<Label> findByGatewayId(long gatewayId, Pageable pageable) {
		return labelRepository.findByGateway_IdOrderByNameDesc(gatewayId,pageable);
	}

	@Override
	public List<Label> findByGatewayIdNoPage(long gatewayId) {
		return labelRepository.findByGateway_IdOrderByNameDesc(gatewayId);
	}

	@Override
	public Label save(Label label) {
		return labelRepository.save(label);
	}

	@Override
	public Optional<Label> findById(long id) {
		return labelRepository.findById(id);
	}

}
