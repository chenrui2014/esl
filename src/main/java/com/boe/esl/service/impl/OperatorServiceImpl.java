package com.boe.esl.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boe.esl.dao.BaseDao;
import com.boe.esl.dao.BoardDao;
import com.boe.esl.dao.OperationDao;
import com.boe.esl.dao.OperatorDao;
import com.boe.esl.model.Board;
import com.boe.esl.model.Operation;
import com.boe.esl.model.Operator;
import com.boe.esl.service.OperatorService;
import com.boe.esl.vo.OperatorVO;

@Transactional
@Service
public class OperatorServiceImpl extends BaseServiceImpl<Operator, Long, OperatorVO> implements OperatorService {

	@Autowired
	private OperatorDao operatorDao;
	
	@Autowired
	private OperationDao operationDao;
	
	private BoardDao boardDao;
	
	@Override
	public OperatorVO convertEntity(Operator operator) {
		OperatorVO operatorVO = new OperatorVO();
		operatorVO.setId(operator.getId());
		operatorVO.setName(operator.getName());
		operatorVO.setCode(operator.getCode());
		operatorVO.setType(operator.getType());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = formatter.format(operator.getLoginTime());
		operatorVO.setLoginTime(formattedDate);
		operatorVO.setStatus(operator.getStatus());
		if(operator.getStatus() == 0) {
			operatorVO.setStatusString("offLine");
		}
		if(operator.getStatus() == 1) {
			operatorVO.setStatusString("onLine");
		}
		Operation operation = operator.getOperation();
		if(operation != null) {
			operatorVO.setOperationId(operation.getId());
			operatorVO.setOperationCode(operation.getCode());
			operatorVO.setOperationName(operation.getName());
		}
		Board board = operator.getBoard();
		if(board != null) {
			operatorVO.setBoardId(board.getId());
			operatorVO.setBoardName(board.getName());
		}
		
		return operatorVO;
	}

	@Override
	public Operator convertVO(OperatorVO operatorVO) {
		Operator operator = new Operator();
		operator.setId(operatorVO.getId());
		operator.setCode(operatorVO.getCode());
		operator.setName(operatorVO.getName());
		operator.setType(operatorVO.getType());
		operator.setStatus(operatorVO.getStatus());
		
		Timestamp ts = new Timestamp(System.currentTimeMillis()); 
		try {   
            ts = Timestamp.valueOf(operatorVO.getLoginTime());     
        } catch (Exception e) {   
            e.printStackTrace();   
        } 
		operator.setLoginTime(ts);
		Operation operation = operationDao.findById(operatorVO.getId()).orElse(null);
		Board board = boardDao.findById(operatorVO.getBoardId()).orElse(null);
		operator.setOperation(operation);
		operator.setBoard(board);
		return operator;
	}

	@Override
	public List<OperatorVO> convertEntityList(List<Operator> operatorList) {
		List<OperatorVO> operatorVOList = new ArrayList<OperatorVO>();
		for (Operator operator : operatorList) {
			OperatorVO operatorVO = convertEntity(operator);
			operatorVOList.add(operatorVO);
		}
		return operatorVOList;
	}

	@Override
	public List<Operator> convertVOList(List<OperatorVO> entityVOList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseDao<Operator, Long> getDAO() {
		return this.operatorDao;
	}

}
