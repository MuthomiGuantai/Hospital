package com.bruceycode.Medical_Service.service;

// Service Interface
import com.bruceycode.Medical_Service.model.entity.Nurse;
import java.util.List;
import java.util.Optional;

public interface NurseService {
    Nurse createNurse(Nurse nurse);
    Optional<Nurse> getNurseById(Long id);
    List<Nurse> getAllNurses();
    Nurse updateNurse(Long id, Nurse nurseDetails);
    void deleteNurse(Long id);
    Nurse addPatientToNurse(Long nurseId, Long patientId);
}

// Service Implementation

