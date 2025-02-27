package com.bruceycode.Medical_Service.service;

import com.bruceycode.Medical_Service.model.entity.Doctor;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor createDoctor(Doctor doctor);
    Optional<Doctor> getDoctorById(Long id);
    List<Doctor> getAllDoctors();
    Doctor updateDoctor(Long id, Doctor doctorDetails);
    void deleteDoctor(Long id);
    Doctor addPatientToDoctor(Long doctorId, Long patientId);
}

