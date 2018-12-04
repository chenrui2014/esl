package com.boe.esl.dao;

import com.boe.esl.model.User;

/**
 * 用户DAO
 * @ClassName UserDao
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年9月10日 下午3:22:26
 * @lastUpdate 2018年9月10日 下午3:22:26
 */
public interface UserDao extends BaseDao<User, Long> {

	User findByUserName(String userName);
}
