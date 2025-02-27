package com.bruceycode.Patient_Service.controller;

import com.bruceycode.Patient_Service.model.Admissions;
import com.bruceycode.Patient_Service.service.AdmissionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admissions")
public class AdmissionsController {

    private static final Logger logger = LoggerFactory.getLogger(AdmissionsController.class);

    private final AdmissionsService admissionsService;

    @Autowired
    public AdmissionsController(AdmissionsService admissionsService) {
        this.admissionsService = admissionsService;
        logger.info("AdmissionsController initialized with AdmissionsService");
    }

    @PostMapping
    public ResponseEntity<Admissions> createAdmission(@RequestBody Admissions admission) {
        logger.info("Received POST request to create admission: {}", admission);
        Admissions createdAdmission = admissionsService.createAdmission(admission);
        logger.info("Successfully created admission: {}", createdAdmission);
        return new ResponseEntity<>(createdAdmission, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admissions> getAdmissionById(@PathVariable Long id) {
        logger.info("Received GET request for admission ID: {}", id);
        return admissionsService.getAdmissionById(id)
                .map(admission -> {
                    logger.info("Successfully retrieved admission ID {}: {}", id, admission);
                    return new ResponseEntity<>(admission, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    logger.warn("Admission not found for ID: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @GetMapping
    public ResponseEntity<List<Admissions>> getAllAdmissions() {
        logger.info("Received GET request for all admissions");
        List<Admissions> admissions = admissionsService.getAllAdmissions();
        logger.info("Successfully retrieved {} admissions", admissions.size());
        return new ResponseEntity<>(admissions, HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Admissions>> getAdmissionsByPatientId(@PathVariable Long patientId) {
        logger.info("Received GET request for admissions of patient ID: {}", patientId);
        List<Admissions> admissions = admissionsService.getAdmissionByPatientId(patientId);
        logger.info("Successfully retrieved {} admissions for patient ID: {}", admissions.size(), patientId);
        return new ResponseEntity<>(admissions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admissions> updateAdmission(@PathVariable Long id, @RequestBody Admissions admission) {
        logger.info("Received PUT request to update admission ID {} with details: {}", id, admission);
        try {
            Admissions updatedAdmission = admissionsService.updateAdmission(id, admission);
            logger.info("Successfully updated admission ID {}: {}", id, updatedAdmission);
            return new ResponseEntity<>(updatedAdmission, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.warn("Admission not found for update with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmission(@PathVariable Long id) {
        logger.info("Received DELETE request for admission ID: {}", id);
        try {
            admissionsService.deleteAdmission(id);
            logger.info("Successfully deleted admission ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.warn("Admission not found for deletion with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}