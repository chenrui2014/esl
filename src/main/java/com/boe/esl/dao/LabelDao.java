package com.boe.esl.dao;

import com.boe.esl.model.Label;

import java.util.List;

public interface LabelDao extends BaseDao<Label, Long> {
	Label findByCode(String code);
	Label findByMac(String mac);
	List<Label> findByGateway_Id(Long gatewayId);
}
