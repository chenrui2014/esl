package com.boe.esl.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.boe.esl.model.Goods;

@RepositoryRestResource(collectionResourceRel="goods", path="goods")
public interface GoodsRepository extends PagingAndSortingRepository<Goods, Long> {
	Goods findByLabel_IdOrderByNameDesc(@Param("labelId")long labelId);
}
