package com.bruceycode.Patient_Service.service;

import com.bruceycode.Patient_Service.model.Admissions;

import java.util.List;
import java.util.Optional;

public interface AdmissionsService {
    Admissions createAdmission(Admissions appointments);
    Optional<Admissions> getAdmissionById(Long id);
    List<Admissions> getAllAdmissions();
    List<Admissions> getAdmissionByPatientId(Long patientId);
    Admissions updateAdmission(Long id, Admissions appointments);
    void deleteAdmission(Long id);
}
