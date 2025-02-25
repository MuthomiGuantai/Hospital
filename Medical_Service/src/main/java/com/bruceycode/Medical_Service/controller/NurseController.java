package com.bruceycode.Medical_Service.controller;

import com.bruceycode.Medical_Service.model.Nurse;
import com.bruceycode.Medical_Service.model.Patient;
import com.bruceycode.Medical_Service.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/nurses")
public class NurseController {

    private final NurseService nurseService;

    @Autowired
    public NurseController(NurseService nurseService) {
        this.nurseService = nurseService;
    }

    // Create a new nurse
    @PostMapping("/register")
    public ResponseEntity<Nurse> createNurse(@RequestBody Nurse nurse) {
        Nurse createdNurse = nurseService.createNurse(nurse);
        return new ResponseEntity<>(createdNurse, HttpStatus.CREATED);
    }

    // Get a nurse by ID
    @GetMapping("/{id}")
    public ResponseEntity<Nurse> getNurseById(@PathVariable Long id) {
        Optional<Nurse> nurse = nurseService.getNurseById(id);
        return nurse.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all nurses
    @GetMapping
    public ResponseEntity<List<Nurse>> getAllNurses() {
        List<Nurse> nurses = nurseService.getAllNurses();
        return new ResponseEntity<>(nurses, HttpStatus.OK);
    }

    // Update a nurse
    @PutMapping("/{id}")
    public ResponseEntity<Nurse> updateNurse(@PathVariable Long id,
                                             @RequestBody Nurse nurseDetails) {
        try {
            Nurse updatedNurse = nurseService.updateNurse(id, nurseDetails);
            return new ResponseEntity<>(updatedNurse, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a nurse
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNurse(@PathVariable Long id) {
        try {
            nurseService.deleteNurse(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{nurseId}/patients")
    public ResponseEntity<Nurse> addPatientToNurse(
            @PathVariable Long nurseId,
            @RequestBody Patient patient) {
        Nurse updatedNurse = nurseService.addPatientToNurse(nurseId, patient.getPatientId());
        return new ResponseEntity<>(updatedNurse, HttpStatus.OK);
    }
}