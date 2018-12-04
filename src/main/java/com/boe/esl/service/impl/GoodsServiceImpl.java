package com.boe.esl.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boe.esl.dao.BaseDao;
import com.boe.esl.dao.GoodsDao;
import com.boe.esl.dao.LabelDao;
import com.boe.esl.model.Goods;
import com.boe.esl.model.Label;
import com.boe.esl.service.GoodsService;
import com.boe.esl.vo.GoodsVO;

@Transactional
@Service
public class GoodsServiceImpl extends BaseServiceImpl<Goods, Long, GoodsVO> implements GoodsService {

	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private LabelDao labelDao;

	@Override
	public GoodsVO convertEntity(Goods goods) {
		GoodsVO goodsVO = new GoodsVO();
		goodsVO.setId(goods.getId());
		goodsVO.setName(goods.getName());
		Label label = goods.getLabel();
		if(label != null) {
			goodsVO.setLabelId(label.getId());
			goodsVO.setLabelName(label.getName());
			goodsVO.setLabelMac(label.getMac());
		}
		goodsVO.setPrice(goods.getPrice());
		goodsVO.setUnit(goods.getUnit());
		
		return goodsVO;
	}

	@Override
	public Goods convertVO(GoodsVO goodsVO) {
		Goods goods = new Goods();
		goods.setId(goodsVO.getId());
		goods.setName(goodsVO.getName());
		goods.setPrice(goodsVO.getPrice());
		goods.setUnit(goodsVO.getUnit());
		Label label = labelDao.findById(goodsVO.getLabelId()).orElse(null);
		goods.setLabel(label);
		return goods;
	}

	@Override
	public List<GoodsVO> convertEntityList(List<Goods> goodsList) {
		List<GoodsVO> goodsVOList = new ArrayList<GoodsVO>();
		for (Goods goods : goodsList) {
			GoodsVO goodsVO = convertEntity(goods);
			goodsVOList.add(goodsVO);
		}
		return goodsVOList;
	}

	@Override
	public List<Goods> convertVOList(List<GoodsVO> entityVOList) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public BaseDao<Goods, Long> getDAO() {
		return this.goodsDao;
	}

	@Override
	public Goods findByLabelId(long labelId) {
		return goodsDao.findByLabel_Id(labelId);
	}

}
