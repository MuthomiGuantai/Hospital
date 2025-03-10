package com.bruceycode.Patient_Service.service.helper;

import com.bruceycode.Patient_Service.model.MedicalRecord;
import com.bruceycode.Patient_Service.repository.MedicalRecordRepository;
import com.bruceycode.Patient_Service.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;
    private final HttpServletRequest request; // Added for JWT forwarding

    private String getMedicalServiceUrl() {
        log.debug("Resolving MEDICAL_SERVICE URI");
        List<ServiceInstance> instances = discoveryClient.getInstances("medical_service");
        if (instances.isEmpty()) {
            log.error("No instances of 'medical_service' found in discovery server");
            throw new RuntimeException("Medical_Service not available");
        }
        String url = instances.get(0).getUri().toString();
        log.info("Resolved MEDICAL_SERVICE URI: {}", url);
        return url;
    }

    private HttpHeaders getAuthHeaders() {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            log.error("No valid Authorization header found");
            throw new RuntimeException("JWT token missing");
        }
        String jwtToken = header.substring(7);
        log.debug("Forwarding JWT token: {}", jwtToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        return headers;
    }

    private void validatePatientExists(Long patientId) {
        if (patientId == null) {
            log.error("Patient ID is null during validation");
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        log.info("Validating patient ID: {}", patientId);
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
            restTemplate.exchange(
                    getMedicalServiceUrl() + "/patients/" + patientId, HttpMethod.GET, requestEntity, Object.class);
            log.debug("Patient ID {} validated successfully", patientId);
        } catch (Exception e) {
            log.error("Patient does not exist with ID {}: {}", patientId, e.getMessage());
            throw new RuntimeException("Patient validation failed: " + e.getMessage());
        }
    }

    private void validateDoctorExists(Long doctorId) {
        if (doctorId == null) {
            log.error("Doctor ID is null during validation");
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }
        log.info("Validating doctor ID: {}", doctorId);
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
            restTemplate.exchange(
                    getMedicalServiceUrl() + "/doctors/" + doctorId, HttpMethod.GET, requestEntity, Object.class);
            log.debug("Doctor ID {} validated successfully", doctorId);
        } catch (Exception e) {
            log.error("Doctor does not exist with ID {}: {}", doctorId, e.getMessage());
            throw new RuntimeException("Doctor validation failed: " + e.getMessage());
        }
    }

    @Override
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        log.info("Creating medical record: {}", medicalRecord);
        validatePatientExists(medicalRecord.getPatientId());
        validateDoctorExists(medicalRecord.getDoctorId());
        MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medicalRecord);
        log.info("Successfully created medical record with ID {}: {}", savedMedicalRecord.getId(), savedMedicalRecord);
        return savedMedicalRecord;
    }

    @Override
    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        log.info("Received request for medical record with ID: {}", id);
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
        if (medicalRecord.isPresent()) {
            log.info("Successfully retrieved medical record with ID {}: {}", id, medicalRecord.get());
        } else {
            log.warn("Medical record not found for ID: {}", id);
        }
        return medicalRecord;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        log.info("Fetching all medical records");
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        log.info("Successfully fetched {} medical records", medicalRecords.size());
        return medicalRecords;
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        log.info("Fetching medical records for patient ID: {}", patientId);
        validatePatientExists(patientId);
        List<MedicalRecord> records = medicalRecordRepository.findByPatientId(patientId);
        log.info("Successfully fetched {} medical records for patient ID: {}", records.size(), patientId);
        return records;
    }

    @Override
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord medicalRecordDetails) {
        log.info("Updating medical record with ID {}: {}", id, medicalRecordDetails);
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findById(id);
        if (optionalMedicalRecord.isPresent()) {
            MedicalRecord medicalRecord = optionalMedicalRecord.get();
            validatePatientExists(medicalRecordDetails.getPatientId());
            validateDoctorExists(medicalRecordDetails.getDoctorId());
            medicalRecord.setPatientId(medicalRecordDetails.getPatientId());
            medicalRecord.setDoctorId(medicalRecordDetails.getDoctorId());
            medicalRecord.setCondition(medicalRecordDetails.getCondition());
            medicalRecord.setDiagnosisDate(medicalRecordDetails.getDiagnosisDate());
            medicalRecord.setNotes(medicalRecordDetails.getNotes());
            MedicalRecord updatedMedicalRecord = medicalRecordRepository.save(medicalRecord);
            log.info("Successfully updated medical record with ID {}: {}", id, updatedMedicalRecord);
            return updatedMedicalRecord;
        }
        log.error("Medical record not found with ID: {}", id);
        throw new RuntimeException("Medical record not found with ID: " + id);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        log.info("Deleting medical record with ID: {}", id);
        if (medicalRecordRepository.existsById(id)) {
            medicalRecordRepository.deleteById(id);
            log.info("Successfully deleted medical record with ID: {}", id);
        } else {
            log.error("Medical record not found with ID: {}", id);
            throw new RuntimeException("Medical record not found with ID: " + id);
        }
    }
}