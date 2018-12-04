//package com.boe.esl.graphql.datafetcher;
//
//import java.util.List;
//
//import org.springframework.stereotype.Component;
//
//import com.boe.esl.model.Gateway;
//import com.boe.esl.graphql.repository.GatewayRepository;
//
//import graphql.schema.DataFetcher;
//import graphql.schema.DataFetchingEnvironment;
//
//@Component
//public class GatewayListDataFetcher implements DataFetcher<List<Gateway>> {
//
//	private GatewayRepository gatewayRepo;
//	@Override
//	public List<Gateway> get(DataFetchingEnvironment environment) {
//		return gatewayRepo.findAll();
//	}
//
//}
