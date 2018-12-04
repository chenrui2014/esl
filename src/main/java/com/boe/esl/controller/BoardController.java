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
import com.boe.esl.model.Board;
import com.boe.esl.service.BoardService;
import com.boe.esl.vo.BoardVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/{version}/boards")
@RestController
@Slf4j
@Api(tags = "看板类", value = "看板类")
public class BoardController {

	@Autowired
	private BoardService boardService;

	@ApiOperation(value = "获得所有看板", notes = "返回看板列表")
	@ApiVersion(1)
	@GetMapping(value = "")
	public RestResult<List<BoardVO>> getAllGateway() {
		return RestResultGenerator.genSuccessResult(boardService.convertEntityList(boardService.findAll()));
	}
	
	@ApiOperation(value = "分页获得看板", notes = "返回看板列表")
	@ApiVersion(1)
	@GetMapping(value = "/boards")
	public PageRestResult<List<BoardVO>> getBoardPerPage(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "name");
		Page<Board> boardPage = boardService.findAll(pageable);
		return RestResultGenerator.genPageResult(boardService.convertEntityList(boardPage.getContent()),
				boardPage.getNumber(), boardPage.getSize(), boardPage.getTotalElements(),
				boardPage.getTotalPages());
	}
	
	@ApiVersion(1)
	@GetMapping(value = "/{id}")
	public RestResult<BoardVO> getBoard(@PathVariable("id") long id) {
		return RestResultGenerator.genSuccessResult(boardService.convertEntity(boardService.findById(id)));
	}

	@ApiVersion(1)
	@PostMapping(value = "")
	public RestResult<BoardVO> addBoard(@RequestBody BoardVO boardVO) {
		return RestResultGenerator
				.genSuccessResult(boardService.convertEntity(boardService.save(boardService.convertVO(boardVO))));
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}")
	public RestResult<BoardVO> updateBoard(@PathVariable("id") Long id, @RequestBody BoardVO boardVO) {
		boardVO.setId(id);
		return RestResultGenerator
				.genSuccessResult(boardService.convertEntity(boardService.save(boardService.convertVO(boardVO))));
	}

	@ApiVersion(1)
	@SuppressWarnings("rawtypes")
	@DeleteMapping(value = "/{id}")
	public RestResult removeBoard(@PathVariable("id") Long id) {
		boardService.del(id);
		return RestResultGenerator.genSuccessResult();
	}
}
