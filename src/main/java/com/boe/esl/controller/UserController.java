package com.boe.esl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boe.esl.annotation.ApiVersion;
import com.boe.esl.exception.BusinessException;
import com.boe.esl.model.User;
import com.boe.esl.service.UserService;
import com.boe.esl.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/{version}/users")
@RestController
@Slf4j
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ApiVersion(1)
	@PostMapping(value="/login")
	public RestResult<UserVO> login(@RequestBody User user) {
		User user2 = userService.findByUserName(user.getUserName());
		if(user2 != null) {
			if(user2.getPassword().equals(user.getPassword())) {
				return RestResultGenerator.genSuccessResult(userService.convertEntity(user2));
			}else {
				log.warn(user.getPassword() + "密码不正确");
				throw new BusinessException(ResultEnum.LOGIN_USERNAME_OR_PASSWORD_ERROR.getCode(), ResultEnum.LOGIN_USERNAME_OR_PASSWORD_ERROR.getMsg());
			}
			
		}else {
			throw new BusinessException(ResultEnum.LOGIN_USER_NOT_EXIST_ERROR.getCode(), ResultEnum.LOGIN_USER_NOT_EXIST_ERROR.getMsg());
		}
	}
	
	@ApiVersion(1)
	@GetMapping(value = "/users")
	public PageRestResult<List<UserVO>> getUserPerPage(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "id");
		Page<User> userPage = userService.findAll(pageable);
		return RestResultGenerator.genPageResult(userService.convertEntityList(userPage.getContent()), 
				userPage.getNumber(), userPage.getSize(), userPage.getTotalElements(), userPage.getTotalPages());
	}

	@ApiVersion(1)
	@GetMapping(value = "")
	public RestResult<List<UserVO>> getAllUser() {
		return RestResultGenerator.genSuccessResult(userService.convertEntityList(userService.findAll()));
	}
	
	@ApiVersion(1)
	@GetMapping(value = "/{id}")
	public RestResult<UserVO> getUser(@PathVariable(value = "id") Long id) {
		return RestResultGenerator
				.genSuccessResult(userService.convertEntity(userService.findById(id)));
	}
	
	@ApiVersion(1)
	@PostMapping(value="")
	public RestResult<UserVO>addUpdate(@RequestBody UserVO userVO){
		return RestResultGenerator.genSuccessResult(userService.convertEntity(userService.save(userService.convertVO(userVO))));
	}
	
	@ApiVersion(1)
	@PutMapping(value="/{id}")
	public RestResult<UserVO> userUpdate(@PathVariable(value = "id") Long id, @RequestBody UserVO userVO){	
		return RestResultGenerator.genSuccessResult(userService.convertEntity(userService.save(userService.convertVO(userVO))));
	}
	
	@SuppressWarnings("rawtypes")
	@ApiVersion(1)
	@DeleteMapping(value="/{id}")
	public RestResult removeUser(@PathVariable("id")Long id){
		userService.del(id);
		return RestResultGenerator.genSuccessResult();
	}
}
