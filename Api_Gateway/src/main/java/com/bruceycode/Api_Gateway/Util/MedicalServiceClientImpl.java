package com.bruceycode.Api_Gateway.Util;

import com.bruceycode.Api_Gateway.Client.MedicalServiceClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MedicalServiceClientImpl implements MedicalServiceClient {
    private final WebClient webClient;
    private final LoadBalancerClient loadBalancerClient;

    public MedicalServiceClientImpl(WebClient.Builder webClientBuilder, LoadBalancerClient loadBalancerClient) {
        this.webClient = webClientBuilder.build();
        this.loadBalancerClient = loadBalancerClient;
    }

    private String getMedicalServiceUrl() {
        ServiceInstance instance = loadBalancerClient.choose("MEDICAL_SERVICE");
        if (instance == null) {
            throw new RuntimeException("No MEDICAL_SERVICE instance available");
        }
        return instance.getUri().toString();
    }

    @Override
    public Mono<String> getPatient(Long patientId) {
        return webClient.get()
                .uri(getMedicalServiceUrl() + "/patients/{id}", patientId)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Error fetching patient: " + e.getMessage()));
    }

    @Override
    public Mono<String> getDoctor(Long doctorId) {
        return webClient.get()
                .uri(getMedicalServiceUrl() + "/doctors/{id}", doctorId)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Error fetching doctor: " + e.getMessage()));
    }
}