package com.boe.esl.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boe.esl.dao.BaseDao;
import com.boe.esl.dao.OperationDao;
import com.boe.esl.model.Operation;
import com.boe.esl.service.OperationService;
import com.boe.esl.vo.OperationVO;

@Transactional
@Service
public class OperationServiceImpl extends BaseServiceImpl<Operation, Long, OperationVO> implements OperationService {

	@Autowired
	private OperationDao operationDao;

	@Override
	public OperationVO convertEntity(Operation operation) {
		OperationVO operationVO = new OperationVO();
		operationVO.setId(operation.getId());
		operationVO.setName(operation.getName());
		operationVO.setCode(operation.getCode());
		operationVO.setContent(operation.getContent());
		
		return operationVO;
	}

	@Override
	public Operation convertVO(OperationVO operationVO) {
		Operation operation = new Operation();
		operation.setId(operationVO.getId());
		operation.setCode(operationVO.getCode());
		operation.setName(operationVO.getName());
		operation.setContent(operationVO.getContent());
		return operation;
	}

	@Override
	public List<OperationVO> convertEntityList(List<Operation> operationList) {
		List<OperationVO> operationVOList = new ArrayList<OperationVO>();
		for (Operation operation : operationList) {
			OperationVO operationVO = convertEntity(operation);
			operationVOList.add(operationVO);
		}
		return operationVOList;
	}

	@Override
	public List<Operation> convertVOList(List<OperationVO> entityVOList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseDao<Operation, Long> getDAO() {
		return this.operationDao;
	}

}
