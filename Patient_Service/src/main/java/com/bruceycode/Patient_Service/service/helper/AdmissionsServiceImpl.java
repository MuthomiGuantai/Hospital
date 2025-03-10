package com.bruceycode.Patient_Service.service.helper;

import com.bruceycode.Patient_Service.model.Admissions;
import com.bruceycode.Patient_Service.repository.AdmissionsRepository;
import com.bruceycode.Patient_Service.service.AdmissionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdmissionsServiceImpl implements AdmissionsService {

    private final AdmissionsRepository admissionsRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;


    private String getMedicalServiceUrl() {
        log.debug("Resolving MEDICAL_SERVICE URI");
        String url = discoveryClient.getInstances("medical_service").get(0).getUri().toString();
        log.info("Resolved MEDICAL_SERVICE URI: {}", url);
        return url;
    }

    private void validatePatientExists(Long patientId) {
        log.info("Validating patient ID: {}", patientId);
        try {
            restTemplate.getForObject(getMedicalServiceUrl() + "/patients/" + patientId, Object.class);
            log.debug("Patient ID {} validated successfully", patientId);
        } catch (Exception e) {
            log.error("Failed to validate patient ID {}: {}", patientId, e.getMessage());
            throw e;
        }
    }

    @Override
    public Admissions createAdmission(Admissions admission) {
        log.info("Creating admission: {}", admission);
        validatePatientExists(admission.getPatientId());
        Admissions savedAdmission = admissionsRepository.save(admission);
        log.info("Successfully created admission: {}", savedAdmission);
        return savedAdmission;
    }

    @Override
    public Optional<Admissions> getAdmissionById(Long id) {
        log.info("Fetching admission by ID: {}", id);
        Optional<Admissions> admission = admissionsRepository.findById(id);
        if (admission.isPresent()) {
            log.info("Successfully fetched admission ID {}: {}", id, admission.get());
        } else {
            log.warn("No admission found for ID: {}", id);
        }
        return admission;
    }

    @Override
    public List<Admissions> getAllAdmissions() {
        log.info("Fetching all admissions");
        List<Admissions> admissions = admissionsRepository.findAll();
        log.info("Successfully fetched {} admissions", admissions.size());
        return admissions;
    }

    @Override
    public List<Admissions> getAdmissionByPatientId(Long patientId) {
        log.info("Fetching admissions for patient ID: {}", patientId);
        validatePatientExists(patientId);
        List<Admissions> admissions = admissionsRepository.findByPatientId(patientId);
        log.info("Successfully fetched {} admissions for patient ID: {}", admissions.size(), patientId);
        return admissions;
    }

    @Override
    public Admissions updateAdmission(Long id, Admissions admissionDetails) {
        log.info("Updating admission ID {} with details: {}", id, admissionDetails);
        Optional<Admissions> optionalAdmission = admissionsRepository.findById(id);
        if (optionalAdmission.isPresent()) {
            Admissions admission = optionalAdmission.get();
            validatePatientExists(admissionDetails.getPatientId());
            admission.setPatientId(admissionDetails.getPatientId());
            admission.setDischargeDate(admissionDetails.getDischargeDate());
            admission.setAdmissionDate(admissionDetails.getAdmissionDate());
            admission.setReason(admissionDetails.getReason());
            Admissions updatedAdmission = admissionsRepository.save(admission);
            log.info("Successfully updated admission ID {}: {}", id, updatedAdmission);
            return updatedAdmission;
        }
        log.error("Admission not found for update with ID: {}", id);
        throw new RuntimeException("Admission not found with ID " + id);
    }

    @Override
    public void deleteAdmission(Long id) {
        log.info("Deleting admission with ID: {}", id);
        if (admissionsRepository.existsById(id)) {
            admissionsRepository.deleteById(id);
            log.info("Successfully deleted admission with ID: {}", id);
        } else {
            log.error("Admission not found for deletion with ID: {}", id);
            throw new RuntimeException("Admission not found with ID " + id);
        }
    }
}