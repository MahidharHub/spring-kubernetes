package com.onebank.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	/**
	 *
	 * @param routeLocatorBuilder
	 * @return
	 */



	@Bean
	public RouteLocator oneBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
						.route(p -> p
								.path("/onebank/accounts/**")
								.filters( f -> f.rewritePath("/onebank/accounts/(?<segment>.*)","/${segment}")
										.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
										.circuitBreaker(config -> config.setName("accountsCircuitBreaker")
												.setFallbackUri("forward:/contactSupport")))


								.uri("lb://ACCOUNTS"))
					.route(p -> p
							.path("/onebank/loans/**")
							.filters( f -> f.rewritePath("/onebank/loans/(?<segment>.*)","/${segment}")
									.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
							.uri("lb://LOANS"))
					.route(p -> p
							.path("/onebank/cards/**")
							.filters( f -> f.rewritePath("/onebank/cards/(?<segment>.*)","/${segment}")
									.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
							.uri("lb://CARDS")).build();


	}

}
