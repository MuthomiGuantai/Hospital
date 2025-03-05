package com.bruceycode.Medical_Service.service;

import com.bruceycode.Medical_Service.dto.medical_services.NurseDTO;

import java.util.List;
import java.util.Optional;

public interface NurseService {
    NurseDTO createNurse(NurseDTO nurseDTO);
    Optional<NurseDTO> getNurseById(Long id, boolean includePatients);
    List<NurseDTO> getAllNurses(); // Existing method
    List<NurseDTO> getAllNurses(boolean includePatients); // New method
    NurseDTO updateNurse(Long id, NurseDTO nurseDetails);
    void deleteNurse(Long id);
    NurseDTO addPatientToNurse(Long nurseId, Long patientId);
}