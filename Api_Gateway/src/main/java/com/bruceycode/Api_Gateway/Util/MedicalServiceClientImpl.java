package com.bruceycode.Api_Gateway.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(MedicalServiceClientImpl.class);

    public MedicalServiceClientImpl(WebClient.Builder webClientBuilder, LoadBalancerClient loadBalancerClient) {
        this.webClient = webClientBuilder.build();
        this.loadBalancerClient = loadBalancerClient;
        logger.info("Initialized MedicalServiceClientImpl with LoadBalancerClient");
    }

    private String getMedicalServiceUrl() {
        ServiceInstance instance = loadBalancerClient.choose("MEDICAL_SERVICE");
        if (instance == null) {
            logger.error("No MEDICAL_SERVICE instance available");
            throw new RuntimeException("No MEDICAL_SERVICE instance available");
        }
        String url = instance.getUri().toString();
        logger.info("Resolved MEDICAL_SERVICE URI: {}", url);
        return url;
    }

    @Override
    public Mono<String> getPatient(Long patientId) {
        logger.info("Fetching patient with ID: {}", patientId);
        String uri = getMedicalServiceUrl() + "/patients/{id}";
        return webClient.get()
                .uri(uri, patientId)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> logger.info("Successfully fetched patient ID {}: {}", patientId, response))
                .onErrorResume(e -> {
                    logger.error("Failed to fetch patient ID {} from URI {}: {}", patientId, uri, e.getMessage());
                    return Mono.just("Error fetching patient: " + e.getMessage());
                });
    }

    @Override
    public Mono<String> getDoctor(Long doctorId) {
        logger.info("Fetching doctor with ID: {}", doctorId);
        String uri = getMedicalServiceUrl() + "/doctors/{id}";
        return webClient.get()
                .uri(uri, doctorId)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> logger.info("Successfully fetched doctor ID {}: {}", doctorId, response))
                .onErrorResume(e -> {
                    logger.error("Failed to fetch doctor ID {} from URI {}: {}", doctorId, uri, e.getMessage());
                    return Mono.just("Error fetching doctor: " + e.getMessage());
                });
    }
}