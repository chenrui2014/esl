package com.boe.esl.controller;

import java.util.List;

import javax.websocket.server.PathParam;

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
import com.boe.esl.model.Gateway;
import com.boe.esl.service.GatewayService;
import com.boe.esl.vo.GatewayVO;

//import graphql.ExecutionResult;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/{version}/gateways")
@RestController
@Slf4j
public class GatewayController {

	@Autowired
	private GatewayService gatewayService;

//	private GraphqlService graphqlService;

//	@PostMapping(value = "/graphql/gateway")
//    public ResponseEntity<Object> allItems(@RequestBody String query) {
//		log.info("查询:"+ query);
//        ExecutionResult result = graphqlService.getGraphQL().execute(query);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
	@ApiVersion(1)
	@GetMapping(value = "perpage")
	public PageRestResult<List<GatewayVO>> getGatewayPerPage(@RequestParam(value = "page") Integer page,
			@RequestParam(value = "size") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Direction.DESC, "name");
		Page<Gateway> gatewayPage = gatewayService.findAll(pageable);
		return RestResultGenerator.genPageResult(gatewayService.convertEntityList(gatewayPage.getContent()),
				gatewayPage.getNumber(), gatewayPage.getSize(), gatewayPage.getTotalElements(),
				gatewayPage.getTotalPages());
	}

	@ApiVersion(1)
	@GetMapping(value = "")
	public RestResult<List<GatewayVO>> getAllGateway() {
		return RestResultGenerator.genSuccessResult(gatewayService.convertEntityList(gatewayService.findAll()));
	}
	
	@ApiVersion(1)
	@GetMapping(value = "/{id}")
	public RestResult<GatewayVO> getGateway(@PathVariable("id") long id) {
		return RestResultGenerator.genSuccessResult(gatewayService.convertEntity(gatewayService.findById(id)));
	}

	@ApiVersion(1)
	@PostMapping(value = "")
	public RestResult<GatewayVO> addGateway( @RequestBody GatewayVO gatewayVO) {
		return RestResultGenerator.genSuccessResult(
				gatewayService.convertEntity(gatewayService.save(gatewayService.convertVO(gatewayVO))));
	}

	@ApiVersion(1)
	@PutMapping(value = "/{id}")
	public RestResult<GatewayVO> updateGateway(@PathVariable("id") Long id, @RequestBody GatewayVO gatewayVO) {
		gatewayVO.setId(id);
		return RestResultGenerator.genSuccessResult(
				gatewayService.convertEntity(gatewayService.save(gatewayService.convertVO(gatewayVO))));
	}

	@ApiVersion(1)
	@SuppressWarnings("rawtypes")
	@DeleteMapping(value = "/{id}")
	public RestResult removeGateway(@PathVariable("id") Long id) {
		gatewayService.del(id);
		return RestResultGenerator.genSuccessResult();
	}
}
