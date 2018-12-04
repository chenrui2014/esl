package com.boe.esl.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * DAO基础类
 * @ClassName BaseDao
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月10日 下午3:22:00
 * @lastUpdate 2018年9月10日 下午3:22:00
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseDao<T, ID extends Serializable> extends JpaSpecificationExecutor<T>, JpaRepository<T, ID> {
	
}
