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
import com.boe.esl.model.Operator;
import com.boe.esl.service.OperationService;
import com.boe.esl.service.OperatorService;
import com.boe.esl.vo.OperationVO;
import com.boe.esl.vo.OperatorVO;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/{version}/operators")
@RestController
@Slf4j
public class OperatorController {

	@Autowired
	private OperatorService operatorService;
	
	@Autowired
	private OperationService operationService;
	
	@ApiVersion(1)
	@GetMapping(value = "/operators")
	public PageRestResult<List<OperatorVO>> getOperatorPerPage(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "name");
		Page<Operator> operatorPage = operatorService.findAll(pageable);
		return RestResultGenerator.genPageResult(operatorService.convertEntityList(operatorPage.getContent()),
				operatorPage.getNumber(), operatorPage.getSize(), operatorPage.getTotalElements(), operatorPage.getTotalPages());
	}

	@ApiVersion(1)
	@GetMapping(value = "")
	public RestResult<List<OperatorVO>> getAllOperator() {
		return RestResultGenerator.genSuccessResult(operatorService.convertEntityList(operatorService.findAll()));
	}
	@ApiVersion(1)
	@GetMapping(value = "/{id}")
	public RestResult<OperatorVO> getOperator(@PathVariable(value = "id") Long id) {
		log.info("操作员ID" + id);
		return RestResultGenerator
				.genSuccessResult(operatorService.convertEntity(operatorService.findById(id)));
	}
	/**
	 * 获得操作员工序
	 * @Title getOperation
	 * @Description TODO()
	 * @param id
	 * @return RestResult<OperationVO>
	 * @throws 
	 * @create 2018年9月13日 上午8:37:17
	 * @lastUpdate 2018年9月13日 上午8:37:17
	 */
	@SuppressWarnings("unchecked")
	@ApiVersion(1)
	@GetMapping(value = "/{id}/operation")
	public RestResult<OperationVO> getOperation(@PathVariable(value = "id") Long id){
		Operator operator = operatorService.findById(id);
		if(operator!= null && operator.getOperation() != null) {
			return RestResultGenerator.genSuccessResult(operationService.convertEntity(operator.getOperation()));
		}
		return RestResultGenerator.genSuccessResult();
	}
	
	@ApiVersion(1)
	@PostMapping(value = "")
	public RestResult<OperatorVO> addOperator(@RequestBody OperatorVO operatorVO) {
		log.info("添加操作员");
		return RestResultGenerator.genSuccessResult(
				operatorService.convertEntity(operatorService.save(operatorService.convertVO(operatorVO))));
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}")
	public RestResult<OperatorVO> updateOperator(@PathVariable("id") Long id, @RequestBody OperatorVO operatorVO) {
		return RestResultGenerator.genSuccessResult(
				operatorService.convertEntity(operatorService.save(operatorService.convertVO(operatorVO))));
	}

	@SuppressWarnings("rawtypes")
	@ApiVersion(1)
	@DeleteMapping(value = "/{id}")
	public RestResult removeOperator(@PathVariable("id") Long id) {
		operatorService.del(id);
		return RestResultGenerator.genSuccessResult();
	}
}
