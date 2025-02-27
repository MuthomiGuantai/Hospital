package com.bruceycode.Patient_Service.service.helper;

import com.bruceycode.Patient_Service.model.Admissions;
import com.bruceycode.Patient_Service.repository.AdmissionsRepository;
import com.bruceycode.Patient_Service.service.AdmissionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AdmissionsServiceImpl implements AdmissionsService {

    private static final Logger logger = LoggerFactory.getLogger(AdmissionsServiceImpl.class);

    private final AdmissionsRepository admissionsRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    @Autowired
    public AdmissionsServiceImpl(AdmissionsRepository admissionsRepository, RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.admissionsRepository = admissionsRepository;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
        logger.info("AdmissionsServiceImpl initialized with AdmissionsRepository, RestTemplate, and DiscoveryClient");
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

    @Override
    public Admissions createAdmission(Admissions admission) {
        logger.info("Creating admission: {}", admission);
        validatePatientExists(admission.getPatientId());
        Admissions savedAdmission = admissionsRepository.save(admission);
        logger.info("Successfully created admission: {}", savedAdmission);
        return savedAdmission;
    }

    @Override
    public Optional<Admissions> getAdmissionById(Long id) {
        logger.info("Fetching admission by ID: {}", id);
        Optional<Admissions> admission = admissionsRepository.findById(id);
        if (admission.isPresent()) {
            logger.info("Successfully fetched admission ID {}: {}", id, admission.get());
        } else {
            logger.warn("No admission found for ID: {}", id);
        }
        return admission;
    }

    @Override
    public List<Admissions> getAllAdmissions() {
        logger.info("Fetching all admissions");
        List<Admissions> admissions = admissionsRepository.findAll();
        logger.info("Successfully fetched {} admissions", admissions.size());
        return admissions;
    }

    @Override
    public List<Admissions> getAdmissionByPatientId(Long patientId) {
        logger.info("Fetching admissions for patient ID: {}", patientId);
        validatePatientExists(patientId);
        List<Admissions> admissions = admissionsRepository.findByPatientId(patientId);
        logger.info("Successfully fetched {} admissions for patient ID: {}", admissions.size(), patientId);
        return admissions;
    }

    @Override
    public Admissions updateAdmission(Long id, Admissions admissionDetails) {
        logger.info("Updating admission ID {} with details: {}", id, admissionDetails);
        Optional<Admissions> optionalAdmission = admissionsRepository.findById(id);
        if (optionalAdmission.isPresent()) {
            Admissions admission = optionalAdmission.get();
            validatePatientExists(admissionDetails.getPatientId());
            admission.setPatientId(admissionDetails.getPatientId());
            admission.setDischargeDate(admissionDetails.getDischargeDate());
            admission.setAdmissionDate(admissionDetails.getAdmissionDate());
            admission.setReason(admissionDetails.getReason());
            Admissions updatedAdmission = admissionsRepository.save(admission);
            logger.info("Successfully updated admission ID {}: {}", id, updatedAdmission);
            return updatedAdmission;
        }
        logger.error("Admission not found for update with ID: {}", id);
        throw new RuntimeException("Admission not found with ID " + id);
    }

    @Override
    public void deleteAdmission(Long id) {
        logger.info("Deleting admission with ID: {}", id);
        if (admissionsRepository.existsById(id)) {
            admissionsRepository.deleteById(id);
            logger.info("Successfully deleted admission with ID: {}", id);
        } else {
            logger.error("Admission not found for deletion with ID: {}", id);
            throw new RuntimeException("Admission not found with ID " + id);
        }
    }
}