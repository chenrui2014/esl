//package com.boe.esl.graphql;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.concurrent.CompletableFuture;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Env;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Service;
//
//import com.boe.esl.graphql.datafetcher.GatewayDataFetcher;
//import com.boe.esl.graphql.datafetcher.GatewayListDataFetcher;
//import com.boe.esl.graphql.datafetcher.GatewayNewDataFetcher;
//import com.boe.esl.model.Gateway;
//
//import graphql.GraphQL;
//import graphql.execution.Async;
//import graphql.schema.AsyncDataFetcher;
//import graphql.schema.DataFetcher;
//import graphql.schema.DataFetchingEnvironment;
//import graphql.schema.GraphQLSchema;
//import graphql.schema.idl.RuntimeWiring;
//import graphql.schema.idl.SchemaGenerator;
//import graphql.schema.idl.SchemaParser;
//import graphql.schema.idl.TypeDefinitionRegistry;
//
//@Service
//public class GraphqlService {
//
//	@Value("classpath:schema.graphqls")
//    Resource schemaResource;
//	
//	@Autowired
//	private GatewayDataFetcher gatewayDataFetcher;
//	@Autowired
//	private GatewayListDataFetcher gatewayListDataFetcher;
//	
//	private GatewayNewDataFetcher gatewayNewDataFetcher;
//	
//	private GraphQL graphQL;
//	
//	@PostConstruct
//    public void loadGraphQLSchema() throws IOException {
//        File schema = schemaResource.getFile();
//        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schema);
//        RuntimeWiring runtimeWiring = initWiring();
//        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry,runtimeWiring);
//        //DataFetcher<CompletableFuture<Object>> gatewayDataFetcher = AsyncDataFetcher.async(env -> findBydId(env.getArgument("id")));
//        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
//    }
//
//    private Object findBydId(Object argument) {
//		return gatewayDataFetcher.get((DataFetchingEnvironment) argument);
//	}
//
//	private Object fetchGatewayHttp(Object argument) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	private RuntimeWiring initWiring() {
//
//        return RuntimeWiring.newRuntimeWiring()
//                .type("Query", typeWiring -> typeWiring
//                .dataFetcher("findAllGateway", gatewayListDataFetcher)
//                .dataFetcher("findBydId", gatewayDataFetcher))
//                .type("Mutation", typeWiring -> typeWiring
//                .dataFetcher("newGateway", gatewayNewDataFetcher)).build();
//    }
//
//    public GraphQL getGraphQL() {
//        return graphQL;
//    }
//}
