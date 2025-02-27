package com.bruceycode.Medical_Service.controller;

import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
        logger.info("PatientController initialized with PatientService");
    }

    // Create a new patient
    @PostMapping("/register")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        logger.info("Received POST request to create patient: {}", patient);
        Patient createdPatient = patientService.createPatient(patient);
        logger.info("Successfully created patient: {}", createdPatient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    // Get a patient by ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        logger.info("Received GET request for patient ID: {}", id);
        Optional<Patient> patient = patientService.getPatientById(id);
        if (patient.isPresent()) {
            logger.info("Successfully retrieved patient ID {}: {}", id, patient.get());
            return new ResponseEntity<>(patient.get(), HttpStatus.OK);
        } else {
            logger.warn("Patient not found for ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get all patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        logger.info("Received GET request for all patients");
        List<Patient> patients = patientService.getAllPatients();
        logger.info("Successfully retrieved {} patients", patients.size());
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    // Update a patient
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id,
                                                 @RequestBody Patient patientDetails) {
        logger.info("Received PUT request to update patient ID {} with details: {}", id, patientDetails);
        try {
            Patient updatedPatient = patientService.updatePatient(id, patientDetails);
            logger.info("Successfully updated patient ID {}: {}", id, updatedPatient);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.warn("Patient not found for update with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a patient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        logger.info("Received DELETE request for patient ID: {}", id);
        try {
            patientService.deletePatient(id);
            logger.info("Successfully deleted patient ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.warn("Patient not found for deletion with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Add doctor to patient
    @PostMapping("/{patientId}/doctors/{doctorId}")
    public ResponseEntity<Patient> addDoctorToPatient(@PathVariable Long patientId,
                                                      @PathVariable Long doctorId) {
        logger.info("Received POST request to add doctor ID {} to patient ID: {}", doctorId, patientId);
        try {
            Patient updatedPatient = patientService.addDoctorToPatient(patientId, doctorId);
            logger.info("Successfully added doctor ID {} to patient ID {}: {}", doctorId, patientId, updatedPatient);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Failed to add doctor ID {} to patient ID {}: {}", doctorId, patientId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Add nurse to patient
    @PostMapping("/{patientId}/nurses/{nurseId}")
    public ResponseEntity<Patient> addNurseToPatient(@PathVariable Long patientId,
                                                     @PathVariable Long nurseId) {
        logger.info("Received POST request to add nurse ID {} to patient ID: {}", nurseId, patientId);
        try {
            Patient updatedPatient = patientService.addNurseToPatient(patientId, nurseId);
            logger.info("Successfully added nurse ID {} to patient ID {}: {}", nurseId, patientId, updatedPatient);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Failed to add nurse ID {} to patient ID {}: {}", nurseId, patientId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}