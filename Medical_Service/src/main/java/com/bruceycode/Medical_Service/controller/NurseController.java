package com.bruceycode.Medical_Service.controller;

import com.bruceycode.Medical_Service.model.entity.Nurse;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.service.NurseService;
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
@RequestMapping("/nurses")
public class NurseController {


    private final NurseService nurseService;


    @PostMapping("/register")
    public ResponseEntity<Nurse> createNurse(@RequestBody Nurse nurse) {
        log.info("Received POST request to create nurse: {}", nurse);
        if (nurse.getName() == null || nurse.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name cannot be null or empty");
        }
        Nurse createdNurse = nurseService.createNurse(nurse);
        log.info("Successfully created nurse: {}", createdNurse);
        return new ResponseEntity<>(createdNurse, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Nurse> getNurseById(@PathVariable Long id) {
        log.info("Received GET request for nurse ID: {}", id);
        Optional<Nurse> nurse = nurseService.getNurseById(id);
        if (nurse.isPresent()) {
            log.info("Successfully retrieved nurse ID {}: {}", id, nurse.get());
            return new ResponseEntity<>(nurse.get(), HttpStatus.OK);
        } else {
            log.warn("Nurse not found for ID: {}", id);
            throw new RuntimeException("Nurse not found for ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<Nurse>> getAllNurses() {
        log.info("Received GET request for all nurses");
        List<Nurse> nurses = nurseService.getAllNurses();
        log.info("Successfully retrieved {} nurses", nurses.size());
        return new ResponseEntity<>(nurses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nurse> updateNurse(@PathVariable Long id,
                                             @RequestBody Nurse nurseDetails) {
        log.info("Received PUT request to update nurse ID {} with details: {}", id, nurseDetails);
        Nurse updatedNurse = nurseService.updateNurse(id, nurseDetails);
        log.info("Successfully updated nurse ID {}: {}", id, updatedNurse);
        return new ResponseEntity<>(updatedNurse, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNurse(@PathVariable Long id) {
        log.info("Received DELETE request for nurse ID: {}", id);
        nurseService.deleteNurse(id);
        log.info("Successfully deleted nurse ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{nurseId}/patients")
    public ResponseEntity<Nurse> addPatientToNurse(
            @PathVariable Long nurseId,
            @RequestBody Patient patient) {
        log.info("Received POST request to add patient ID {} to nurse ID: {}", patient.getPatientId(), nurseId);
        Nurse updatedNurse = nurseService.addPatientToNurse(nurseId, patient.getPatientId());
        log.info("Successfully added patient ID {} to nurse ID {}: {}", patient.getPatientId(), nurseId, updatedNurse);
        return new ResponseEntity<>(updatedNurse, HttpStatus.OK);
    }
}