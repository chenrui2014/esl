package com.boe.esl.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.boe.esl.model.Gateway;

@RepositoryRestResource(collectionResourceRel="gateway", path="gateway")
public interface GatewayRepository extends PagingAndSortingRepository<Gateway, Long> {

}
