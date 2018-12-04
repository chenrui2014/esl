//package com.boe.esl.graphql.datafetcher;
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
//public class GatewayDataFetcher implements DataFetcher<Gateway> {
//
//	private GatewayRepository gatewayRepo;
//	@Override
//	public Gateway get(DataFetchingEnvironment environment) {
//		Long id = environment.getArgument("id");
//		return gatewayRepo.findOne(id);
//	}
//
//}
