package com.boe.esl.dao;

import com.boe.esl.model.Gateway;

public interface GatewayDao extends BaseDao<Gateway, Long> {

	Gateway findByNameAndKey(String name, String key);
	Gateway findByKey(String key);
}
