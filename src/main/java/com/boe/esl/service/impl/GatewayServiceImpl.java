package com.boe.esl.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.boe.esl.model.APStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boe.esl.dao.BaseDao;
import com.boe.esl.dao.GatewayDao;
import com.boe.esl.model.Gateway;
import com.boe.esl.service.GatewayService;
import com.boe.esl.vo.GatewayVO;

@Transactional
@Service
public class GatewayServiceImpl extends BaseServiceImpl<Gateway, Long, GatewayVO> implements GatewayService {

	@Autowired
	private GatewayDao gatewayDao;

	@Override
	public GatewayVO convertEntity(Gateway gateway) {
		GatewayVO gatewayVO = new GatewayVO();
		gatewayVO.setId(gateway.getId());
		gatewayVO.setKey(gateway.getKey());
		gatewayVO.setName(gateway.getName());
		gatewayVO.setIp(gateway.getIp());
		gatewayVO.setMac(gateway.getMac());
		gatewayVO.setStatus(gateway.getStatus());

		int status = 0;
		if (gateway.getStatus() != null) {
			status = gateway.getStatus();
		}
		for(APStatus apStatus: APStatus.values()){
			if(apStatus.getCode() == status){
				gatewayVO.setStatusText(apStatus.getMsg());
				break;
			}
		}
		
		return gatewayVO;
	}

	@Override
	public Gateway convertVO(GatewayVO gatewayVO) {
		Gateway gateway = new Gateway();
		gateway.setId(gatewayVO.getId());
		gateway.setKey(gatewayVO.getKey());
		gateway.setName(gatewayVO.getName());
		gateway.setIp(gatewayVO.getIp());
		gateway.setMac(gatewayVO.getMac());
		gateway.setStatus(gatewayVO.getStatus());
		return gateway;
	}

	@Override
	public List<GatewayVO> convertEntityList(List<Gateway> gatewayList) {
		List<GatewayVO> gatewayVOList = new ArrayList<GatewayVO>();
		for (Gateway gateway : gatewayList) {
			GatewayVO gatewayVO = convertEntity(gateway);
			gatewayVOList.add(gatewayVO);
		}
		return gatewayVOList;
	}

	@Override
	public List<Gateway> convertVOList(List<GatewayVO> entityVOList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseDao<Gateway, Long> getDAO() {
		return this.gatewayDao;
	}

	@Override
	public Gateway findByNameAndKey(Gateway gateway) {
		if(gateway != null) {
			return gatewayDao.findByNameAndKey(gateway.getName(), gateway.getKey());
		}
		return null;
	}

	@Override
	public Gateway getGatewayByKey(String key) {
		return gatewayDao.findByKey(key);
	}

}
