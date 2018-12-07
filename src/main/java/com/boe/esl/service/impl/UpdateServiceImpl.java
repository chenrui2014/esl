package com.boe.esl.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.boe.esl.dao.LabelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boe.esl.dao.BaseDao;
import com.boe.esl.dao.GoodsDao;
import com.boe.esl.dao.UpdateDao;
import com.boe.esl.model.Goods;
import com.boe.esl.model.Label;
import com.boe.esl.model.Update;
import com.boe.esl.service.UpdateService;
import com.boe.esl.vo.UpdateVO;

@Transactional
@Service
public class UpdateServiceImpl extends BaseServiceImpl<Update, Long, UpdateVO> implements UpdateService {

	@Autowired
	private UpdateDao updateDao;
	
	@Autowired
	private LabelDao labelDao;
	
	@Override
	public UpdateVO convertEntity(Update update) {
		UpdateVO updateVO = new UpdateVO();
		updateVO.setId(update.getId());
		Label label = update.getLabel();
		if(label != null) {
			updateVO.setLabelId(label.getId());
			updateVO.setLabelCode(label.getCode());
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = formatter.format(update.getUpdateTime());
		updateVO.setUpdateTime(formattedDate);
		update.setBarCode(updateVO.getBarCode());
		update.setCustomJson(updateVO.getCustomJson());
		update.setIsOk(updateVO.getIsOk());
		update.setMaterialName(updateVO.getMaterialName());
		update.setMaterialNum(updateVO.getMaterialNum());
		update.setSid(updateVO.getSid());
		return updateVO;
	}

	@Override
	public Update convertVO(UpdateVO updateVO) {
		Update update = new Update();
		update.setId(updateVO.getId());
		Timestamp ts = new Timestamp(System.currentTimeMillis()); 
		try {   
            ts = Timestamp.valueOf(updateVO.getUpdateTime());     
        } catch (Exception e) {   
            e.printStackTrace();   
        } 
		update.setUpdateTime(ts);
		update.setBarCode(updateVO.getBarCode());
		update.setCustomJson(updateVO.getCustomJson());
		update.setIsOk(updateVO.getIsOk());
		update.setMaterialName(updateVO.getMaterialName());
		update.setMaterialNum(updateVO.getMaterialNum());
		update.setSid(updateVO.getSid());

		if(updateVO.getLabelId() != null){
			Label label = labelDao.findById(updateVO.getLabelId()).orElse(null);
			if(label != null){
				update.setLabel(label);
			}
		}
		return update;
	}

	@Override
	public List<UpdateVO> convertEntityList(List<Update> updateList) {
		List<UpdateVO> updateVOList = new ArrayList<UpdateVO>();
		for (Update update : updateList) {
			UpdateVO updateVO = convertEntity(update);
			updateVOList.add(updateVO);
		}
		return updateVOList;
	}

	@Override
	public List<Update> convertVOList(List<UpdateVO> userVOList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseDao<Update, Long> getDAO() {
		return this.updateDao;
	}

	@Override
	public Update getLatestUpdateByLabelCode(String labelCode) {
		return updateDao.findFirstByLabel_CodeOrderByUpdateTimeDesc(labelCode);
	}
}
