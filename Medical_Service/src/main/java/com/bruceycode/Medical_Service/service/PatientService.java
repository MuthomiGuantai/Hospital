package com.bruceycode.Medical_Service.service;

import com.bruceycode.Medical_Service.dto.medical_services.PatientDTO;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    PatientDTO createPatient(PatientDTO patientDTO);
    Optional<PatientDTO> getPatientById(Long id);
    List<PatientDTO> getAllPatients();
    PatientDTO updatePatient(Long id, PatientDTO patientDetails);
    void deletePatient(Long id);
    PatientDTO addDoctorToPatient(Long patientId, Long doctorId);
    PatientDTO addNurseToPatient(Long patientId, Long nurseId);
}