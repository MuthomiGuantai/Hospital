package com.bruceycode.Patient_Service.repository;

import com.bruceycode.Patient_Service.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
}
