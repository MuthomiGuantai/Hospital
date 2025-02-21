package com.bruceycode.Medical_Service.service;
// Service Interface

import com.bruceycode.Medical_Service.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient createPatient(Patient patient);
    Optional<Patient> getPatientById(Long id);
    List<Patient> getAllPatients();
    Patient updatePatient(Long id, Patient patientDetails);
    void deletePatient(Long id);
    Patient addDoctorToPatient(Long patientId, Long doctorId);
    Patient addNurseToPatient(Long patientId, Long nurseId);
}
