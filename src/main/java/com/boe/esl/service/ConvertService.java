package com.boe.esl.service;

import java.util.List;

public interface ConvertService<T,V> {

	V convertEntity(T entity);
	T convertVO(V entityVO);
	List<V> convertEntityList(List<T> entityList);
	List<T> convertVOList(List<V> entityVOList);
}
