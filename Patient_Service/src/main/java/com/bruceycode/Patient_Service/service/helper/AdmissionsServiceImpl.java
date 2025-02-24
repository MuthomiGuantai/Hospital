package com.bruceycode.Patient_Service.service.helper;

import com.bruceycode.Patient_Service.model.Admissions;
import com.bruceycode.Patient_Service.repository.AdmissionsRepository;
import com.bruceycode.Patient_Service.service.AdmissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AdmissionsServiceImpl implements AdmissionsService {

    private final AdmissionsRepository admissionsRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    @Autowired
    public AdmissionsServiceImpl(AdmissionsRepository admissionsRepository, RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.admissionsRepository = admissionsRepository;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    private String getMedicalServiceUrl() {
        return discoveryClient.getInstances("medical_service").get(0).getUri().toString();
    }

    private void validatePatientExists(Long patientId) {
        restTemplate.getForObject(getMedicalServiceUrl() + "/patients/" + patientId, Object.class);
    }


    @Override
    public Admissions createAdmission(Admissions admission) {
        validatePatientExists(admission.getPatientId());
        return admissionsRepository.save(admission);
    }

    @Override
    public Optional<Admissions> getAdmissionById(Long id) {
        return admissionsRepository.findById(id);
    }

    @Override
    public List<Admissions> getAllAdmissions() {
        return admissionsRepository.findAll();
    }

    @Override
    public List<Admissions> getAdmissionByPatientId(Long patientId) {
        validatePatientExists(patientId);
        return admissionsRepository.findByPatientId(patientId);
    }

    @Override
    public Admissions updateAdmission(Long id, Admissions admissionDetails) {
        Optional<Admissions> optionalAdmission = admissionsRepository.findById(id);
        if (optionalAdmission.isPresent()) {
            Admissions admission = optionalAdmission.get();
            validatePatientExists(admissionDetails.getPatientId());
            admission.setPatientId(admissionDetails.getPatientId());
            admission.setDischargeDate(admissionDetails.getDischargeDate());
            admission.setAdmissionDate(admissionDetails.getAdmissionDate());
            admission.setReason(admissionDetails.getReason());
            return admissionsRepository.save(admission);
        }
        throw new RuntimeException("Admission not found with ID " + id);
    }

    @Override
    public void deleteAdmission(Long id) {
        if (admissionsRepository.existsById(id)) {
            admissionsRepository.deleteById(id);
        } else {
            throw new RuntimeException("Admission not found with ID " + id);
        }
    }
}