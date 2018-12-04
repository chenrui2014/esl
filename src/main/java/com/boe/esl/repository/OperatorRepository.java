package com.boe.esl.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.boe.esl.model.Operator;

@Deprecated
@RepositoryRestResource(collectionResourceRel="operator", path="operator")
public interface OperatorRepository extends PagingAndSortingRepository<Operator, Long> {
	
}
