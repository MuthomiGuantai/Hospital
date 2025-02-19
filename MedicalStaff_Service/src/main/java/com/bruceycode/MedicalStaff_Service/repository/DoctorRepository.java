package com.bruceycode.MedicalStaff_Service.repository;

import com.bruceycode.MedicalStaff_Service.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository <Doctor, Long> {
}
