package com.bruceycode.Medical_Service.controller;

import com.bruceycode.Medical_Service.model.entity.Nurse;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.service.NurseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/nurses")
public class NurseController {

    private static final Logger logger = LoggerFactory.getLogger(NurseController.class);

    private final NurseService nurseService;

    @Autowired
    public NurseController(NurseService nurseService) {
        this.nurseService = nurseService;
        logger.info("NurseController initialized with NurseService");
    }

    // Create a new nurse
    @PostMapping("/register")
    public ResponseEntity<Nurse> createNurse(@RequestBody Nurse nurse) {
        logger.info("Received POST request to create nurse: {}", nurse);
        Nurse createdNurse = nurseService.createNurse(nurse);
        logger.info("Successfully created nurse: {}", createdNurse);
        return new ResponseEntity<>(createdNurse, HttpStatus.CREATED);
    }

    // Get a nurse by ID
    @GetMapping("/{id}")
    public ResponseEntity<Nurse> getNurseById(@PathVariable Long id) {
        logger.info("Received GET request for nurse ID: {}", id);
        Optional<Nurse> nurse = nurseService.getNurseById(id);
        if (nurse.isPresent()) {
            logger.info("Successfully retrieved nurse ID {}: {}", id, nurse.get());
            return new ResponseEntity<>(nurse.get(), HttpStatus.OK);
        } else {
            logger.warn("Nurse not found for ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get all nurses
    @GetMapping
    public ResponseEntity<List<Nurse>> getAllNurses() {
        logger.info("Received GET request for all nurses");
        List<Nurse> nurses = nurseService.getAllNurses();
        logger.info("Successfully retrieved {} nurses", nurses.size());
        return new ResponseEntity<>(nurses, HttpStatus.OK);
    }

    // Update a nurse
    @PutMapping("/{id}")
    public ResponseEntity<Nurse> updateNurse(@PathVariable Long id,
                                             @RequestBody Nurse nurseDetails) {
        logger.info("Received PUT request to update nurse ID {} with details: {}", id, nurseDetails);
        try {
            Nurse updatedNurse = nurseService.updateNurse(id, nurseDetails);
            logger.info("Successfully updated nurse ID {}: {}", id, updatedNurse);
            return new ResponseEntity<>(updatedNurse, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.warn("Nurse not found for update with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a nurse
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNurse(@PathVariable Long id) {
        logger.info("Received DELETE request for nurse ID: {}", id);
        try {
            nurseService.deleteNurse(id);
            logger.info("Successfully deleted nurse ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.warn("Nurse not found for deletion with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{nurseId}/patients")
    public ResponseEntity<Nurse> addPatientToNurse(
            @PathVariable Long nurseId,
            @RequestBody Patient patient) {
        logger.info("Received POST request to add patient ID {} to nurse ID: {}", patient.getPatientId(), nurseId);
        try {
            Nurse updatedNurse = nurseService.addPatientToNurse(nurseId, patient.getPatientId());
            logger.info("Successfully added patient ID {} to nurse ID {}: {}", patient.getPatientId(), nurseId, updatedNurse);
            return new ResponseEntity<>(updatedNurse, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Failed to add patient ID {} to nurse ID {}: {}", patient.getPatientId(), nurseId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}