package com.bruceycode.Medical_Service.controller;

import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Create a new patient
    @PostMapping("/register")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient createdPatient = patientService.createPatient(patient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    // Get a patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.getPatientById(id);
        return patient.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    // Update a patient
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id,
                                                 @RequestBody Patient patientDetails) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patientDetails);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a patient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Add doctor to patient
    @PostMapping("/{patientId}/doctors/{doctorId}")
    public ResponseEntity<Patient> addDoctorToPatient(@PathVariable Long patientId,
                                                      @PathVariable Long doctorId) {
        try {
            Patient updatedPatient = patientService.addDoctorToPatient(patientId, doctorId);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Add nurse to patient
    @PostMapping("/{patientId}/nurses/{nurseId}")
    public ResponseEntity<Patient> addNurseToPatient(@PathVariable Long patientId,
                                                     @PathVariable Long nurseId) {
        try {
            Patient updatedPatient = patientService.addNurseToPatient(patientId, nurseId);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}