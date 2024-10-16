package org.example;

import org.springframework.cloud.gateway.filter.factory.AddRequestHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/employee/**")
                        .filters(
                                f -> {
                            return f.addRequestHeader("x-test", "bar");
                        })
                        .uri("http://localhost:8081/"))
                        .build();
//        return builder.routes()
//                .route(r -> r.path("/employee/**")
//                        .filters(f -> f.filter((exchange, chain) -> {
//                            // Mutate the request to ensure headers are mutable
//                            ServerHttpRequest mutableRequest = exchange.getRequest().mutate()
//                                    .header("x-instana-test", "bar")
//                                    .build();
//
//                            return chain.filter(exchange.mutate().request(mutableRequest).build());
//                        }))
//                        .uri("http://localhost:8081/"))
//                .build();

//        return chain.filter(exchange.mutate().request(mutableRequest).build());
    }

}
