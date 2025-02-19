package com.bruceycode.MedicalStaff_Service.service;

import com.bruceycode.MedicalStaff_Service.model.Pharmacist;
import com.bruceycode.MedicalStaff_Service.repository.PharmacistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PharmacistService {

    @Autowired
    private PharmacistRepository pharmacistRepository;

    // Create a new pharmacist
    public Pharmacist createPharmacist(Pharmacist pharmacist) {
        return pharmacistRepository.save(pharmacist);
    }

    // Get all pharmacists
    public List<Pharmacist> getAllPharmacists() {
        return pharmacistRepository.findAll();
    }

    // Get a pharmacist by ID
    public Optional<Pharmacist> getPharmacistById(Long id) {
        return pharmacistRepository.findById(id);
    }

    // Update a pharmacist
    public Pharmacist updatePharmacist(Long id, Pharmacist pharmacistDetails) {
        Pharmacist pharmacist = pharmacistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found with id: " + id));

        pharmacist.setName(pharmacistDetails.getName());
        pharmacist.setEmail(pharmacistDetails.getEmail());
        pharmacist.setPhone_number(pharmacistDetails.getPhone_number());

        return pharmacistRepository.save(pharmacist);
    }

    // Delete a pharmacist
    public void deletePharmacist(Long id) {
        Pharmacist pharmacist = pharmacistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found with id: " + id));
        pharmacistRepository.delete(pharmacist);
    }
}