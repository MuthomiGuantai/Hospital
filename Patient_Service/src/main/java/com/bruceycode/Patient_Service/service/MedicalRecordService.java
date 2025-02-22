package com.bruceycode.Patient_Service.service;

import com.bruceycode.Patient_Service.model.MedicalRecord;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordService {
    MedicalRecord createMedicalRecord(MedicalRecord medicalRecord);
    Optional<MedicalRecord> getMedicalRecordById(Long id);
    List<MedicalRecord> getAllMedicalRecords();
    List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId);
    MedicalRecord updateMedicalRecord(Long id, MedicalRecord medicalRecord);
    void deleteMedicalRecord(Long id);
}