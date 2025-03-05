package com.bruceycode.Medical_Service.service;

import com.bruceycode.Medical_Service.dto.medical_services.DoctorDTO;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO doctorDTO);
    Optional<DoctorDTO> getDoctorById(Long id, boolean includePatients);
    List<DoctorDTO> getAllDoctors(); // Existing method
    List<DoctorDTO> getAllDoctors(boolean includePatients); // New method
    DoctorDTO updateDoctor(Long id, DoctorDTO doctorDetails);
    void deleteDoctor(Long id);
    DoctorDTO addPatientToDoctor(Long doctorId, Long patientId);
}