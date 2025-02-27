package com.bruceycode.Api_Gateway.Client;

import reactor.core.publisher.Mono;

public interface MedicalServiceClient {
    Mono<String> getPatient(Long patientId);
    Mono<String> getDoctor(Long doctorId);
}