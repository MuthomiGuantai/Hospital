package com.bruceycode.MedicalStaff_Service.repository;

import com.bruceycode.MedicalStaff_Service.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacistRepository extends JpaRepository<Patient, Long> {
}
