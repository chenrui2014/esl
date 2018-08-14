package com.boe.esl.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.boe.esl.model.Update;

@RepositoryRestResource(collectionResourceRel="update", path="update")
public interface UpdateRepository extends PagingAndSortingRepository<Update, Long> {

	List<Update> findByLabel_IdOrderById(@Param("labelId")long labelId);
	Page<Update> findByLabel_IdOrderById(@Param("labelId")long labelId,Pageable pageable);
}
