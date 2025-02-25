package com.bruceycode.Api_Gateway.Util;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class LoadBalancerGatewayFilter implements GatewayFilter {
    private final LoadBalancerClient loadBalancerClient;

    public LoadBalancerGatewayFilter(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        java.net.URI originalUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        if (originalUri != null && "lb".equals(originalUri.getScheme())) {
            String serviceId = originalUri.getHost(); // e.g., MEDICAL_SERVICE
            ServiceInstance instance = loadBalancerClient.choose(serviceId);
            if (instance == null) {
                return Mono.error(new RuntimeException("No instance available for " + serviceId));
            }
            // Reconstruct the URI with the instance’s host and port
            java.net.URI resolvedUri = loadBalancerClient.reconstructURI(instance, originalUri);
            // Update the exchange with the resolved URI
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, resolvedUri);
            // Ensure the request URI reflects the resolved value
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(exchange.getRequest().mutate().uri(resolvedUri).build())
                    .build();
            return chain.filter(modifiedExchange); // Pass modified exchange
        }
        return chain.filter(exchange);
    }
}