package com.boe.esl.dao;

import com.boe.esl.model.Label;

public interface LabelDao extends BaseDao<Label, Long> {
	Label findByCode(String code);
}
