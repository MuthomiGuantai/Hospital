package com.bruceycode.Patient_Service.service.helper;

import com.bruceycode.Patient_Service.model.MedicalRecord;
import com.bruceycode.Patient_Service.repository.MedicalRecordRepository;
import com.bruceycode.Patient_Service.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    @Autowired
    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository, RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    private String getMedicalServiceUrl() {
        return discoveryClient.getInstances("medical_service").get(0).getUri().toString();
    }

    private void validatePatientExists(Long patientId) {
        restTemplate.getForObject(getMedicalServiceUrl() + "/patients/" + patientId, Object.class);
    }

    private void validateDoctorExists(Long doctorId) {
        if (doctorId != null) {
            restTemplate.getForObject(getMedicalServiceUrl() + "/doctors/" + doctorId, Object.class);
        }
    }

    @Override
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        validatePatientExists(medicalRecord.getPatientId());
        validateDoctorExists(medicalRecord.getDoctorId());
        return medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        validatePatientExists(patientId);
        return medicalRecordRepository.findByPatientId(patientId);
    }

    @Override
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord medicalRecordDetails) {
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
            return medicalRecordRepository.save(record);
        }
        throw new RuntimeException("Medical Record not found with ID " + id);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        if (medicalRecordRepository.existsById(id)) {
            medicalRecordRepository.deleteById(id);
        } else {
            throw new RuntimeException("Medical Record not found with ID " + id);
        }
    }
}