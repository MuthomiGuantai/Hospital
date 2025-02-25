package com.bruceycode.Api_Gateway.Util;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

public class LoadBalancerGatewayFilter implements GatewayFilter {
    private final LoadBalancerClient loadBalancerClient;

    public LoadBalancerGatewayFilter(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        System.out.println("Route: " + (route != null ? route.getId() : "null")); // Debug
        if (route == null) {
            System.out.println("No route found in exchange"); // Debug
            return chain.filter(exchange);
        }

        URI originalUri = route.getUri();
        System.out.println("Original URI: " + originalUri); // Debug
        if (originalUri == null || !"lb".equals(originalUri.getScheme())) {
            System.out.println("URI is null or not lb:// scheme: " + originalUri); // Debug
            return chain.filter(exchange);
        }

        String serviceId = originalUri.getHost();
        if (serviceId == null) {
            String uriString = originalUri.toString();
            if (uriString.startsWith("lb://")) {
                serviceId = uriString.substring("lb://".length()).split("/")[0];
            }
            System.out.println("Extracted serviceId from URI: " + serviceId); // Debug
            if (serviceId == null || serviceId.isEmpty()) {
                return Mono.error(new RuntimeException("Service ID is null or empty for URI: " + originalUri));
            }
        }

        ServiceInstance instance = loadBalancerClient.choose(serviceId);
        if (instance == null) {
            System.out.println("No instance available for " + serviceId); // Debug
            return Mono.error(new RuntimeException("No instance available for " + serviceId));
        }

        String resolvedUrl = instance.getUri().toString() + exchange.getRequest().getPath().value();
        URI resolvedUri = URI.create(resolvedUrl);
        System.out.println("Resolved URI: " + resolvedUri); // Debug

        Route modifiedRoute = Route.async()
                .id(route.getId())
                .uri(resolvedUri)
                .order(route.getOrder())
                .asyncPredicate(route.getPredicate())
                .filters(route.getFilters())
                .metadata(route.getMetadata())
                .build();

        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR, modifiedRoute);
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, resolvedUri);
        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate().uri(resolvedUri).build())
                .build();
        System.out.println("Modified URI: " + modifiedExchange.getRequest().getURI()); // Debug
        return chain.filter(modifiedExchange);
    }
}