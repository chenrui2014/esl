//package com.boe.esl.graphql.datafetcher;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.boe.esl.graphql.repository.GatewayRepository;
//import com.boe.esl.model.Gateway;
//
//import graphql.schema.DataFetcher;
//import graphql.schema.DataFetchingEnvironment;
//
//@Component
//public class GatewayNewDataFetcher implements DataFetcher<Gateway> {
//
//	@Autowired
//	private GatewayRepository gatewayRepo;
//	@Override
//	public Gateway get(DataFetchingEnvironment environment) {
//		String name = environment.getArgument("name");
//		String key = environment.getArgument("key");
//		Boolean online = environment.getArgument("online");
//		Gateway gateway = new Gateway();
//		gateway.setName(name);
//		gateway.setKey(key);
//		gateway.setOnline(online);
//		return gatewayRepo.save(gateway);
//	}
//
//}
