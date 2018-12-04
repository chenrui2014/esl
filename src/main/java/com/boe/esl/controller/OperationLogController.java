package com.boe.esl.controller;

import com.boe.esl.annotation.ApiVersion;
import com.boe.esl.model.OperationLog;
import com.boe.esl.service.OperationLogService;
import com.boe.esl.vo.OperationLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/{version}/operationLogs")
@RestController
@Slf4j
public class OperationLogController {
    @Autowired
    private OperationLogService operationLogService;

    @ApiVersion(1)
    @GetMapping(value = "perpage")
    public PageRestResult<List<OperationLogVO>> getOperationLogPerPage(@RequestParam(value = "page") Integer page,
                                                         @RequestParam(value = "size") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "name");
        Page<OperationLog> labelPage = operationLogService.findAll(pageable);
        return RestResultGenerator.genPageResult(operationLogService.convertEntityList(labelPage.getContent()),
                labelPage.getNumber(), labelPage.getSize(), labelPage.getTotalElements(), labelPage.getTotalPages());
    }

    @ApiVersion(1)
    @GetMapping(value = "")
    public RestResult<List<OperationLogVO>> getAllOperationLog() {
        return RestResultGenerator.genSuccessResult(operationLogService.convertEntityList(operationLogService.findAll()));
    }

    @ApiVersion(1)
    @GetMapping(value = "/{id}")
    public RestResult<OperationLogVO> getOperationLog(@PathVariable("id") long id) {
        return RestResultGenerator.genSuccessResult(operationLogService.convertEntity(operationLogService.findById(id)));
    }

    @ApiVersion(1)
    @PostMapping(value = "")
    public RestResult<OperationLogVO> addOperationLog(@RequestBody OperationLogVO labelVO) {
        return RestResultGenerator
                .genSuccessResult(operationLogService.convertEntity(operationLogService.save(operationLogService.convertVO(labelVO))));
    }

    @ApiVersion(1)
    @PutMapping(value = "/{id}")
    public RestResult<OperationLogVO> updateOperationLog(@PathVariable("id") long id, @RequestBody OperationLogVO labelVO) {
        labelVO.setId(id);
        return RestResultGenerator
                .genSuccessResult(operationLogService.convertEntity(operationLogService.save(operationLogService.convertVO(labelVO))));
    }

    @SuppressWarnings("rawtypes")
    @ApiVersion(1)
    @DeleteMapping(value = "/{id}")
    public RestResult removeOperationLog(@PathVariable("id") Long id) {
        operationLogService.del(id);
        return RestResultGenerator.genSuccessResult();
    }
}
