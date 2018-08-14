package com.boe.esl.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.boe.esl.model.Label;

@RepositoryRestResource(collectionResourceRel="label", path="label")
public interface LabelRepository extends PagingAndSortingRepository<Label, Long> {

	List<Label> findByGateway_IdOrderByNameDesc(@Param("gatewayId")long gatewayId);
	Page<Label> findByGateway_IdOrderByNameDesc(@Param("gatewayId")long gatewayId,Pageable pageable);
}
