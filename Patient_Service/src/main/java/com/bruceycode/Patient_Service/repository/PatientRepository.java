package com.bruceycode.Patient_Service.repository;

import com.bruceycode.Patient_Service.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
