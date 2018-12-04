package com.boe.esl.service;

import com.boe.esl.model.User;
import com.boe.esl.vo.UserVO;

public interface UserService extends BaseService<User, Long, UserVO> {

	User findByUserName(String userName);
}
