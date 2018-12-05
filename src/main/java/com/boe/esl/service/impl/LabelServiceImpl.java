package com.boe.esl.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.boe.esl.model.LabelStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boe.esl.dao.BaseDao;
import com.boe.esl.dao.GatewayDao;
import com.boe.esl.dao.LabelDao;
import com.boe.esl.model.Gateway;
import com.boe.esl.model.Label;
import com.boe.esl.service.LabelService;
import com.boe.esl.vo.LabelVO;

@Transactional
@Service
public class LabelServiceImpl extends BaseServiceImpl<Label, Long, LabelVO> implements LabelService {

	@Autowired
	private LabelDao labelDao;
	
	@Autowired
	private GatewayDao gatewayDao;

	@Override
	public LabelVO convertEntity(Label label) {
		LabelVO labelVO = new LabelVO();
		labelVO.setId(label.getId());
		labelVO.setName(label.getName());
		labelVO.setMac(label.getMac());
		labelVO.setCode(label.getCode());
		labelVO.setPower(label.getPower());
		labelVO.setType(label.getType());
		labelVO.setStatus(label.getStatus());
		int status = 0;
		if (label.getStatus() != null) {
			status = label.getStatus();
		}
		for(LabelStatus apStatus: LabelStatus.values()){
			if(apStatus.getCode() == status){
				labelVO.setStatusText(apStatus.getMsg());
				break;
			}
		}
		Gateway gateway = label.getGateway();
		if(gateway != null) {
			labelVO.setGatewayId(gateway.getId());
			labelVO.setGatewayMac(gateway.getMac());
		}
		
		return labelVO;
	}

	@Override
	public Label convertVO(LabelVO labelVO) {
		Label label = new Label();
		label.setId(labelVO.getId());
		label.setCode(labelVO.getCode());
		label.setPower(labelVO.getPower());
		label.setType(labelVO.getType());
		label.setMac(labelVO.getMac());
		label.setName(labelVO.getName());

		label.setStatus(labelVO.getStatus());
		Gateway gateway = gatewayDao.findById(labelVO.getGatewayId()).orElse(null);
		if(gateway != null) {
			label.setGateway(gateway);
		}
		return label;
	}

	@Override
	public List<LabelVO> convertEntityList(List<Label> labelList) {
		List<LabelVO> labelVOList = new ArrayList<LabelVO>();
		for (Label label : labelList) {
			LabelVO labelVO = convertEntity(label);
			labelVOList.add(labelVO);
		}
		return labelVOList;
	}

	@Override
	public List<Label> convertVOList(List<LabelVO> entityVOList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseDao<Label, Long> getDAO() {
		return this.labelDao;
	}

	@Override
	public Label getLabelByCode(String code) {
		Label label = labelDao.findByCode(code);
		if(label != null){
			return label;
		}
		return null;
	}
}
