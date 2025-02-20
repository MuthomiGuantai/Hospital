package com.bruceycode.MedicalStaff_Service.controller;

import com.bruceycode.MedicalStaff_Service.model.Patient;
import com.bruceycode.MedicalStaff_Service.service.PatientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacists")
public class PharmacistController {

    @Autowired
    private PatientServiceImpl patientServiceImpl;

    // Create a new pharmacist
    @PostMapping("/register")
    public Patient createPharmacist(@RequestBody Patient patient) {
        return patientServiceImpl.createPharmacist(patient);
    }

    // Get all pharmacists
    @GetMapping
    public List<Patient> getAllPharmacists() {
        return patientServiceImpl.getAllPharmacists();
    }

    // Get a pharmacist by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPharmacistById(@PathVariable Long id) {
        return patientServiceImpl.getPharmacistById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a pharmacist
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePharmacist(@PathVariable Long id, @RequestBody Patient patientDetails) {
        Patient updatedPatient = patientServiceImpl.updatePharmacist(id, patientDetails);
        return ResponseEntity.ok(updatedPatient);
    }

    // Delete a pharmacist
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePharmacist(@PathVariable Long id) {
        patientServiceImpl.deletePharmacist(id);
        return ResponseEntity.noContent().build();
    }
}