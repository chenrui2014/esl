package com.boe.esl.service;

import com.boe.esl.model.Update;
import com.boe.esl.vo.UpdateVO;

public interface UpdateService extends BaseService<Update, Long, UpdateVO> {

    Update getLatestUpdateByLabelCode(String labelCode);
}
