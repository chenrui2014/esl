package com.boe.esl.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.boe.esl.model.User;

@Deprecated
@RepositoryRestResource(collectionResourceRel="user",path="user")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	User findByUserNameAndPassword(String userName, String password);
}
