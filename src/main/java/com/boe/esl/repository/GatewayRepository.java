package com.boe.esl.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.boe.esl.model.Gateway;

@Deprecated
@RepositoryRestResource(collectionResourceRel="gateway", path="gateway")
public interface GatewayRepository extends PagingAndSortingRepository<Gateway, Long> {
	Gateway findByNameAndKey(String gatewayName, String key);
}
