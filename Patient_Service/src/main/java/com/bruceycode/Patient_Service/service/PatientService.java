package com.bruceycode.Patient_Service.service;

import com.bruceycode.Patient_Service.model.Patient;
import com.bruceycode.Patient_Service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Create a new patient
    public Patient registerPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // Get all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Get a patient by ID
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // Update a patient
    public Patient updatePatient(Long id, Patient updatedPatient) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setName(updatedPatient.getName());
                    patient.setDob(updatedPatient.getDob());
                    patient.setGender(updatedPatient.getGender());
                    patient.setPhone_number(updatedPatient.getPhone_number());
                    patient.setEmail(updatedPatient.getEmail());
                    patient.setMedical_history(updatedPatient.getMedical_history());
                    return patientRepository.save(patient);
                })
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    // Delete a patient
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}