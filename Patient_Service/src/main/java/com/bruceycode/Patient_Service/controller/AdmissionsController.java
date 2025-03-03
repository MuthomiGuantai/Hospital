package com.bruceycode.Patient_Service.controller;

import com.bruceycode.Patient_Service.exception.NotFoundException;
import com.bruceycode.Patient_Service.model.Admissions;
import com.bruceycode.Patient_Service.service.AdmissionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admissions")
public class AdmissionsController {

    private final AdmissionsService admissionsService;

    @PostMapping
    public ResponseEntity<Admissions> createAdmission(@RequestBody Admissions admission) {
        log.info("Received POST request to create admission: {}", admission);
        Admissions createdAdmission = admissionsService.createAdmission(admission);
        log.info("Successfully created admission: {}", createdAdmission);
        return new ResponseEntity<>(createdAdmission, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admissions> getAdmissionById(@PathVariable Long id) {
        log.info("Received GET request for admission ID: {}", id);
        Optional<Admissions> admission = admissionsService.getAdmissionById(id);
        if (admission.isPresent()) {
            log.info("Successfully retrieved admission ID {}: {}", id, admission.get());
            return new ResponseEntity<>(admission.get(), HttpStatus.OK);
        } else {
            log.warn("Admission not found for ID: {}", id);
            throw new NotFoundException("Admission not found with ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<Admissions>> getAllAdmissions() {
        log.info("Received GET request for all admissions");
        List<Admissions> admissions = admissionsService.getAllAdmissions();
        log.info("Successfully retrieved {} admissions", admissions.size());
        return new ResponseEntity<>(admissions, HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Admissions>> getAdmissionsByPatientId(@PathVariable Long patientId) {
        log.info("Received GET request for admissions of patient ID: {}", patientId);
        List<Admissions> admissions = admissionsService.getAdmissionByPatientId(patientId);
        log.info("Successfully retrieved {} admissions for patient ID: {}", admissions.size(), patientId);
        return new ResponseEntity<>(admissions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admissions> updateAdmission(@PathVariable Long id, @RequestBody Admissions admission) {
        log.info("Received PUT request to update admission ID {} with details: {}", id, admission);
        Admissions updatedAdmission = admissionsService.updateAdmission(id, admission);
        log.info("Successfully updated admission ID {}: {}", id, updatedAdmission);
        return new ResponseEntity<>(updatedAdmission, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmission(@PathVariable Long id) {
        log.info("Received DELETE request for admission ID: {}", id);
        admissionsService.deleteAdmission(id);
        log.info("Successfully deleted admission ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}