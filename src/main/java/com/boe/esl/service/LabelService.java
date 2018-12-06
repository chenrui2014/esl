package com.boe.esl.service;

import com.boe.esl.model.Label;
import com.boe.esl.vo.LabelVO;

import java.util.List;

public interface LabelService extends BaseService<Label, Long, LabelVO> {

    Label getLabelByCode(String code);
    Label getLabelByMac(String mac);
    List<Label> getLabelListByGateway(Long gatewayId);
}
