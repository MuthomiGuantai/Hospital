package com.bruceycode.Patient_Service.service.helper;

import com.bruceycode.Patient_Service.model.MedicalRecord;
import com.bruceycode.Patient_Service.repository.MedicalRecordRepository;
import com.bruceycode.Patient_Service.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordServiceImpl.class);

    private final MedicalRecordRepository medicalRecordRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    @Autowired
    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository, RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
        logger.info("MedicalRecordServiceImpl initialized with MedicalRecordRepository, RestTemplate, and DiscoveryClient");
    }

    private String getMedicalServiceUrl() {
        logger.debug("Resolving MEDICAL_SERVICE URI");
        String url = discoveryClient.getInstances("medical_service").get(0).getUri().toString();
        logger.info("Resolved MEDICAL_SERVICE URI: {}", url);
        return url;
    }

    private void validatePatientExists(Long patientId) {
        logger.info("Validating patient ID: {}", patientId);
        try {
            restTemplate.getForObject(getMedicalServiceUrl() + "/patients/" + patientId, Object.class);
            logger.debug("Patient ID {} validated successfully", patientId);
        } catch (Exception e) {
            logger.error("Failed to validate patient ID {}: {}", patientId, e.getMessage());
            throw e;
        }
    }

    private void validateDoctorExists(Long doctorId) {
        if (doctorId != null) {
            logger.info("Validating doctor ID: {}", doctorId);
            try {
                restTemplate.getForObject(getMedicalServiceUrl() + "/doctors/" + doctorId, Object.class);
                logger.debug("Doctor ID {} validated successfully", doctorId);
            } catch (Exception e) {
                logger.error("Failed to validate doctor ID {}: {}", doctorId, e.getMessage());
                throw e;
            }
        } else {
            logger.debug("Doctor ID is null, skipping validation");
        }
    }

    @Override
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        logger.info("Creating medical record: {}", medicalRecord);
        validatePatientExists(medicalRecord.getPatientId());
        validateDoctorExists(medicalRecord.getDoctorId());
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        logger.info("Successfully created medical record: {}", savedRecord);
        return savedRecord;
    }

    @Override
    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        logger.info("Fetching medical record by ID: {}", id);
        Optional<MedicalRecord> record = medicalRecordRepository.findById(id);
        if (record.isPresent()) {
            logger.info("Successfully fetched medical record ID {}: {}", id, record.get());
        } else {
            logger.warn("No medical record found for ID: {}", id);
        }
        return record;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        logger.info("Fetching all medical records");
        List<MedicalRecord> records = medicalRecordRepository.findAll();
        logger.info("Successfully fetched {} medical records", records.size());
        return records;
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        logger.info("Fetching medical records for patient ID: {}", patientId);
        validatePatientExists(patientId);
        List<MedicalRecord> records = medicalRecordRepository.findByPatientId(patientId);
        logger.info("Successfully fetched {} medical records for patient ID: {}", records.size(), patientId);
        return records;
    }

    @Override
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord medicalRecordDetails) {
        logger.info("Updating medical record ID {} with details: {}", id, medicalRecordDetails);
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
            logger.info("Successfully updated medical record ID {}: {}", id, updatedRecord);
            return updatedRecord;
        }
        logger.error("Medical record not found for update with ID: {}", id);
        throw new RuntimeException("Medical Record not found with ID " + id);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        logger.info("Deleting medical record with ID: {}", id);
        if (medicalRecordRepository.existsById(id)) {
            medicalRecordRepository.deleteById(id);
            logger.info("Successfully deleted medical record with ID: {}", id);
        } else {
            logger.error("Medical record not found for deletion with ID: {}", id);
            throw new RuntimeException("Medical Record not found with ID " + id);
        }
    }
}