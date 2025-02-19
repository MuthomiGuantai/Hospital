package com.bruceycode.MedicalStaff_Service.service;

import com.bruceycode.MedicalStaff_Service.model.Nurse;
import com.bruceycode.MedicalStaff_Service.repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NurseService {

    @Autowired
    private NurseRepository nurseRepository;

    // Create a new nurse
    public Nurse registerNurse(Nurse nurse) {
        return nurseRepository.save(nurse);
    }

    // Get all nurses
    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    // Get a nurse by ID
    public Optional<Nurse> getNurseById(Long id) {
        return nurseRepository.findById(id);
    }

    // Update a nurse
    public Nurse updateNurse(Long id, Nurse nurseDetails) {
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse not found with id: " + id));

        nurse.setName(nurseDetails.getName());
        nurse.setDepartment(nurseDetails.getDepartment());
        nurse.setPatients(nurseDetails.getPatients());
        nurse.setEmail(nurseDetails.getEmail());
        nurse.setPhone_number(nurseDetails.getPhone_number());

        return nurseRepository.save(nurse);
    }

    // Delete a nurse
    public void deleteNurse(Long id) {
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse not found with id: " + id));
        nurseRepository.delete(nurse);
    }
}