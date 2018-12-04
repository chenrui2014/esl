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
import com.boe.esl.model.Label;
import com.boe.esl.service.LabelService;
import com.boe.esl.vo.LabelVO;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/{version}/labels")
@RestController
@Slf4j
public class LabelController {

	@Autowired
	private LabelService labelService;

	@ApiVersion(1)
	@GetMapping(value = "perpage")
	public PageRestResult<List<LabelVO>> getLabelPerPage(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "name");
		Page<Label> labelPage = labelService.findAll(pageable);
		return RestResultGenerator.genPageResult(labelService.convertEntityList(labelPage.getContent()),
				labelPage.getNumber(), labelPage.getSize(), labelPage.getTotalElements(), labelPage.getTotalPages());
	}

	@ApiVersion(1)
	@GetMapping(value = "")
	public RestResult<List<LabelVO>> getAllLabel() {
		return RestResultGenerator.genSuccessResult(labelService.convertEntityList(labelService.findAll()));
	}
	
	@ApiVersion(1)
	@GetMapping(value = "/{id}")
	public RestResult<LabelVO> getLabel(@PathVariable("id") long id) {
		return RestResultGenerator.genSuccessResult(labelService.convertEntity(labelService.findById(id)));
	}

	@ApiVersion(1)
	@PostMapping(value = "")
	public RestResult<LabelVO> addLabel(@RequestBody LabelVO labelVO) {
		return RestResultGenerator
				.genSuccessResult(labelService.convertEntity(labelService.save(labelService.convertVO(labelVO))));
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}")
	public RestResult<LabelVO> updateLabel(@PathVariable("id") long id, @RequestBody LabelVO labelVO) {
		labelVO.setId(id);
		return RestResultGenerator
				.genSuccessResult(labelService.convertEntity(labelService.save(labelService.convertVO(labelVO))));
	}

	@SuppressWarnings("rawtypes")
	@ApiVersion(1)
	@DeleteMapping(value = "/{id}")
	public RestResult removeLabel(@PathVariable("id") Long id) {
		labelService.del(id);
		return RestResultGenerator.genSuccessResult();
	}
}
