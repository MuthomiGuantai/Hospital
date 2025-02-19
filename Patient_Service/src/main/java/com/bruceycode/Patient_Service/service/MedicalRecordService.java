package com.bruceycode.Patient_Service.service;

import com.bruceycode.Patient_Service.model.MedicalRecord;
import com.bruceycode.Patient_Service.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    // Create a new medical record
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    // Get all medical records
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    // Get a medical record by ID
    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    // Update a medical record
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord updatedMedicalRecord) {
        return medicalRecordRepository.findById(id)
                .map(medicalRecord -> {
                    medicalRecord.setPatient_id(updatedMedicalRecord.getPatient_id());
                    medicalRecord.setDoctor_id(updatedMedicalRecord.getDoctor_id());
                    medicalRecord.setDiagnosis(updatedMedicalRecord.getDiagnosis());
                    medicalRecord.setDate(updatedMedicalRecord.getDate());
                    medicalRecord.setMedication(updatedMedicalRecord.getMedication());
                    medicalRecord.setTest_results(updatedMedicalRecord.getTest_results());
                    return medicalRecordRepository.save(medicalRecord);
                })
                .orElseThrow(() -> new RuntimeException("Medical Record not found with id: " + id));
    }

    // Delete a medical record
    public void deleteMedicalRecord(Long id) {
        medicalRecordRepository.deleteById(id);
    }
}