package com.bruceycode.Medical_Service.controller;

import com.bruceycode.Medical_Service.dto.medical_services.NurseDTO;
import com.bruceycode.Medical_Service.service.NurseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NurseDTO> createNurse(@RequestBody NurseDTO nurseDTO) {
        log.info("Received POST request to create nurse: {}", nurseDTO);
        if (nurseDTO.getName() == null || nurseDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nurse name cannot be null or empty");
        }
        NurseDTO createdNurse = nurseService.createNurse(nurseDTO);
        log.info("Successfully created nurse: {}", createdNurse);
        return new ResponseEntity<>(createdNurse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NurseDTO> getNurseById(@PathVariable Long id) {
        log.info("Received GET request for nurse ID: {}", id);
        Optional<NurseDTO> nurse = nurseService.getNurseById(id, true); // Always include patients
        if (nurse.isPresent()) {
            log.info("Successfully retrieved nurse ID {}: {}", id, nurse.get());
            return new ResponseEntity<>(nurse.get(), HttpStatus.OK);
        } else {
            log.warn("Nurse not found for ID: {}", id);
            throw new RuntimeException("Nurse not found for ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<NurseDTO>> getAllNurses() {
        log.info("Received GET request for all nurses");
        List<NurseDTO> nurses = nurseService.getAllNurses(true); // Always include patients
        log.info("Successfully retrieved {} nurses", nurses.size());
        return new ResponseEntity<>(nurses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NurseDTO> updateNurse(@PathVariable Long id, @RequestBody NurseDTO nurseDetails) {
        log.info("Received PUT request to update nurse ID {} with details: {}", id, nurseDetails);
        NurseDTO updatedNurse = nurseService.updateNurse(id, nurseDetails);
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

    @PostMapping("/{nurseId}/patients/{patientId}")
    public ResponseEntity<NurseDTO> addPatientToNurse(@PathVariable Long nurseId, @PathVariable Long patientId) {
        log.info("Received POST request to add patient ID {} to nurse ID: {}", patientId, nurseId);
        NurseDTO updatedNurse = nurseService.addPatientToNurse(nurseId, patientId);
        log.info("Successfully added patient ID {} to nurse ID {}: {}", patientId, nurseId, updatedNurse);
        return new ResponseEntity<>(updatedNurse, HttpStatus.OK);
    }
}