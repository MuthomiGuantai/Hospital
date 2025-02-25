package com.bruceycode.Api_Gateway;

import com.bruceycode.Api_Gateway.Util.LoadBalancerGatewayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
	private final LoadBalancerClient loadBalancerClient;

	public ApiGatewayApplication(LoadBalancerClient loadBalancerClient) {
		this.loadBalancerClient = loadBalancerClient;
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("medical_service", r -> r.path("/medical/**")
						.filters(f -> f.rewritePath("/medical/(?<remaining>.*)", "/${remaining}")
								.filter(new LoadBalancerGatewayFilter(loadBalancerClient)))
						.uri("lb://MEDICAL_SERVICE"))
				.route("patient_service", r -> r.path("/patient/**")
						.filters(f -> f.rewritePath("/patient/(?<remaining>.*)", "/${remaining}")
								.filter(new LoadBalancerGatewayFilter(loadBalancerClient)))
						.uri("lb://PATIENT_SERVICE"))
				.route("department_service", r -> r.path("/department/**")
						.filters(f -> f.rewritePath("/department/(?<remaining>.*)", "/${remaining}")
								.filter(new LoadBalancerGatewayFilter(loadBalancerClient)))
						.uri("lb://DEPARTMENT_SERVICE"))
				.build();
	}
}