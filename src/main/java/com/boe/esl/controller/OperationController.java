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
import com.boe.esl.model.Operation;
import com.boe.esl.service.OperationService;
import com.boe.esl.service.OperatorService;
import com.boe.esl.vo.OperationVO;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/{version}/operations")
@RestController
@Slf4j
public class OperationController {

	@Autowired
	private OperationService operationService;

	@Autowired
	private OperatorService operatorService;

	@ApiVersion(1)
	@GetMapping(value = "/operations")
	public PageRestResult<List<OperationVO>> getOperationPerPage(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "name");
		Page<Operation> operationPage = operationService.findAll(pageable);
		return RestResultGenerator.genPageResult(operationService.convertEntityList(operationPage.getContent()),
				operationPage.getNumber(), operationPage.getSize(), operationPage.getTotalElements(), operationPage.getTotalPages());
	}

	@ApiVersion(1)
	@GetMapping(value = "")
	public RestResult<List<OperationVO>> getAllOperation() {
		return RestResultGenerator.genSuccessResult(operationService.convertEntityList(operationService.findAll()));
	}
	
	@ApiVersion(1)
	@GetMapping(value = "/{id}")
	public RestResult<OperationVO> getOperation(@PathVariable(value = "id") Long id) {
		return RestResultGenerator
				.genSuccessResult(operationService.convertEntity(operationService.findById(id)));
	}

	@ApiVersion(1)
	@PostMapping(value = "")
	public RestResult<OperationVO> addOperation(@RequestBody OperationVO operationVO) {
		return RestResultGenerator.genSuccessResult(
				operationService.convertEntity(operationService.save(operationService.convertVO(operationVO))));
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}")
	public RestResult<OperationVO> updateOperation(@PathVariable(value = "id") Long id, @RequestBody OperationVO operationVO) {
		operationVO.setId(id);
		return RestResultGenerator.genSuccessResult(
				operationService.convertEntity(operationService.save(operationService.convertVO(operationVO))));
	}

	@ApiVersion(1)
	@SuppressWarnings("rawtypes")
	@DeleteMapping(value = "/{id}")
	public RestResult removeOperation(@PathVariable("id") Long id) {
		operationService.del(id);
		return RestResultGenerator.genSuccessResult();
	}
}
