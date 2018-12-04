package com.boe.esl.dao;

import com.boe.esl.model.Goods;

public interface GoodsDao extends BaseDao<Goods, Long> {

	Goods findByLabel_Id(long labelId);
}
