package com.bruceycode.Patient_Service.service.helper;

import com.bruceycode.Patient_Service.model.MedicalRecord;
import com.bruceycode.Patient_Service.repository.MedicalRecordRepository;
import com.bruceycode.Patient_Service.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

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

    private void validatePatientExists(Long patientId) {
        if (patientId == null) {
            log.error("Patient ID is null during validation");
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        log.info("Validating patient ID: {}", patientId);
        try {
            restTemplate.getForObject(getMedicalServiceUrl() + "/patients/" + patientId, Object.class);
            log.debug("Patient ID {} validated successfully", patientId);
        } catch (Exception e) {
            log.error("Failed to validate patient ID {}: {}", patientId, e.getMessage());
            throw e;
        }
    }

    private void validateDoctorExists(Long doctorId) {
        if (doctorId != null) {
            log.info("Validating doctor ID: {}", doctorId);
            try {
                restTemplate.getForObject(getMedicalServiceUrl() + "/doctors/" + doctorId, Object.class);
                log.debug("Doctor ID {} validated successfully", doctorId);
            } catch (Exception e) {
                log.error("Failed to validate doctor ID {}: {}", doctorId, e.getMessage());
                throw e;
            }
        } else {
            log.debug("Doctor ID is null, skipping validation");
        }
    }

    @Override
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        log.info("Creating medical record: {}", medicalRecord);
        if (medicalRecord.getPatientId() == null) {
            log.error("Patient ID is null in medical record: {}", medicalRecord);
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        validatePatientExists(medicalRecord.getPatientId());
        validateDoctorExists(medicalRecord.getDoctorId());
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        log.info("Successfully created medical record: {}", savedRecord);
        return savedRecord;
    }

    @Override
    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        log.info("Fetching medical record by ID: {}", id);
        Optional<MedicalRecord> record = medicalRecordRepository.findById(id);
        if (record.isPresent()) {
            log.info("Successfully fetched medical record ID {}: {}", id, record.get());
        } else {
            log.warn("No medical record found for ID: {}", id);
        }
        return record;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        log.info("Fetching all medical records");
        List<MedicalRecord> records = medicalRecordRepository.findAll();
        log.info("Successfully fetched {} medical records", records.size());
        return records;
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
        log.info("Updating medical record ID {} with details: {}", id, medicalRecordDetails);
        Optional<MedicalRecord> optionalRecord = medicalRecordRepository.findById(id);
        if (optionalRecord.isPresent()) {
            MedicalRecord record = optionalRecord.get();
            validatePatientExists(medicalRecordDetails.getPatientId());
            validateDoctorExists(medicalRecordDetails.getDoctorId());
            record.setPatientId(medicalRecordDetails.getPatientId());
            record.setDoctorId(medicalRecordDetails.getDoctorId());
            record.setCondition(medicalRecordDetails.getCondition());
            record.setDiagnosisDate(medicalRecordDetails.getDiagnosisDate());
            record.setNotes(medicalRecordDetails.getNotes());
            MedicalRecord updatedRecord = medicalRecordRepository.save(record);
            log.info("Successfully updated medical record ID {}: {}", id, updatedRecord);
            return updatedRecord;
        }
        log.error("Medical record not found for update with ID: {}", id);
        throw new RuntimeException("Medical Record not found with ID " + id);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        log.info("Deleting medical record with ID: {}", id);
        if (medicalRecordRepository.existsById(id)) {
            medicalRecordRepository.deleteById(id);
            log.info("Successfully deleted medical record with ID: {}", id);
        } else {
            log.error("Medical record not found for deletion with ID: {}", id);
            throw new RuntimeException("Medical Record not found with ID " + id);
        }
    }
}