package com.boe.esl.service;

import com.boe.esl.model.Gateway;
import com.boe.esl.vo.GatewayVO;

public interface GatewayService extends BaseService<Gateway, Long, GatewayVO> {

	Gateway findByNameAndKey(Gateway gateway);
	Gateway getGatewayByKey(String key);
}
