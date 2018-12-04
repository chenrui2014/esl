package com.boe.esl.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boe.esl.dao.BaseDao;
import com.boe.esl.dao.UserDao;
import com.boe.esl.model.User;
import com.boe.esl.service.UserService;
import com.boe.esl.vo.UserVO;

@Transactional
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long, UserVO> implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public BaseDao<User, Long> getDAO() {
		return userDao;
	}

	@Override
	public User findByUserName(String userName) {
		return userDao.findByUserName(userName);
	}
	
	@Override
	public UserVO convertEntity(User user) {
		return super.getConverter().execute(new VOCallback<UserVO>() {

			@Override
			public UserVO convert() {
				UserVO userVO = new UserVO();
				userVO.setEmail(user.getEmail());
				userVO.setId(user.getId());
				userVO.setUserName(user.getUserName());
				userVO.setPassword(user.getPassword());
				userVO.setStatus(user.getStatus());
				return userVO;
			}
		});
	}
	
	@Override
	public User convertVO(UserVO userVO) {
		return super.getConverter().execute(new VOCallback<User>() {

			@Override
			public User convert() {
				User user = new User();
				user.setId(userVO.getId());
				user.setEmail(userVO.getEmail());
				user.setUserName(userVO.getUserName());
				user.setPassword(userVO.getPassword());
				user.setStatus(userVO.getStatus());
				return user;
			}
		});
	}
	
	@Override
	public List<UserVO> convertEntityList(List<User> userList){
		return super.getConverter().execute(new VOCallback<List<UserVO>>() {

			@Override
			public List<UserVO> convert() {
				List<UserVO> userVOList = new ArrayList<UserVO>();
				for (User user : userList) {
					UserVO userVO = convertEntity(user);
					userVOList.add(userVO);
				}
				return userVOList;
			}
		});
	}

	@Override
	public List<User> convertVOList(List<UserVO> userVOList) {
		// TODO Auto-generated method stub
		return null;
	}

}
