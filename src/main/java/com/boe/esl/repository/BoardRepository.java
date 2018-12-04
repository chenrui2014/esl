package com.boe.esl.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.boe.esl.model.Board;
@Deprecated
@RepositoryRestResource(collectionResourceRel="board", path="board")
public interface BoardRepository extends PagingAndSortingRepository<Board, Long> {

}
