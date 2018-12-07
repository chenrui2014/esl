package com.boe.esl.dao;

import com.boe.esl.model.Update;

public interface UpdateDao extends BaseDao<Update, Long> {

    Update findFirstByLabel_CodeOrderByUpdateTimeDesc(String labelCode);
}
