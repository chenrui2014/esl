package com.boe.esl.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.boe.esl.model.Order;

@Deprecated
@RepositoryRestResource(collectionResourceRel="order", path="order")
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

	List<Order> findByStatus(Sort sort, String status);
}
