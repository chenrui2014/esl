package com.boe.esl.service;

import com.boe.esl.model.Goods;
import com.boe.esl.vo.GoodsVO;

public interface GoodsService extends BaseService<Goods, Long, GoodsVO> {
	Goods findByLabelId(long labelId);
}
