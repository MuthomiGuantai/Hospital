package com.bruceycode.Patient_Service.repository;

import com.bruceycode.Patient_Service.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {
    List<Appointments> findByPatientId(Long patientId);
}