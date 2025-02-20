package com.bruceycode.MedicalStaff_Service.service;

import com.bruceycode.MedicalStaff_Service.model.Patient;
import com.bruceycode.MedicalStaff_Service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PharmacistService {

    @Autowired
    private PatientRepository patientRepository;

    // Create a new pharmacist
    public Patient createPharmacist(Patient patient) {
        return patientRepository.save(patient);
    }

    // Get all pharmacists
    public List<Patient> getAllPharmacists() {
        return patientRepository.findAll();
    }

    // Get a pharmacist by ID
    public Optional<Patient> getPharmacistById(Long id) {
        return patientRepository.findById(id);
    }

    // Update a pharmacist
    public Patient updatePharmacist(Long id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found with id: " + id));

        patient.setName(patientDetails.getName());
        patient.setEmail(patientDetails.getEmail());
        patient.setPhone_number(patientDetails.getPhone_number());

        return patientRepository.save(patient);
    }

    // Delete a pharmacist
    public void deletePharmacist(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pharmacist not found with id: " + id));
        patientRepository.delete(patient);
    }
}