package com.boe.esl.service.impl;

import com.boe.esl.dao.BaseDao;
import com.boe.esl.dao.OperationLogDao;
import com.boe.esl.model.OperationLog;
import com.boe.esl.service.OperationLogService;
import com.boe.esl.vo.OperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLog, Long, OperationLogVO> implements OperationLogService {

    @Autowired
    private OperationLogDao operationLogDao;

    @Override
    public BaseDao<OperationLog, Long> getDAO() {
        return this.operationLogDao;
    }

    @Override
    public OperationLogVO convertEntity(OperationLog entity) {
        return null;
    }

    @Override
    public OperationLog convertVO(OperationLogVO entityVO) {
        return null;
    }

    @Override
    public List<OperationLogVO> convertEntityList(List<OperationLog> entityList) {
        return null;
    }

    @Override
    public List<OperationLog> convertVOList(List<OperationLogVO> entityVOList) {
        return null;
    }
}
