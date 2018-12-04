package com.boe.esl.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.boe.esl.model.Gateway;

public interface GatewayService2 {
	List<Gateway> findAll();
	List<Gateway> findALl(Sort sort);
	Page<Gateway> findAllPaging(Pageable pageable);
	Gateway save(Gateway gateway);
	Gateway findById(long id);
	void delete(Long id);
	boolean isExist(Gateway gateway);
	Gateway findByNameAndKey(Gateway gateway);
	Gateway findByNameAndKey(String gatewayName, String key);
}
