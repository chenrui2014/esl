package com.boe.esl.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.boe.esl.model.Operation;

@Deprecated
@RepositoryRestResource(collectionResourceRel="operation", path="operation")
public interface OperationRepository extends PagingAndSortingRepository<Operation, Long> {
	
}
