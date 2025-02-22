package com.bruceycode.Patient_Service.repository;

import com.bruceycode.Patient_Service.model.Admissions;
import com.bruceycode.Patient_Service.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdmissionsRepository extends JpaRepository<Admissions, Long> {
    List<Admissions> findByPatientId(Long patientId);
}