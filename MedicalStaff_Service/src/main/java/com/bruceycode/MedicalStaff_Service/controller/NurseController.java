package com.bruceycode.MedicalStaff_Service.controller;

import com.bruceycode.MedicalStaff_Service.model.Nurse;
import com.bruceycode.MedicalStaff_Service.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nurses")
public class NurseController {

    @Autowired
    private NurseService nurseService;

    // Create a new nurse
    @PostMapping("/register")
    public Nurse registerNurse(@RequestBody Nurse nurse) {
        return nurseService.registerNurse(nurse);
    }

    // Get all nurses
    @GetMapping
    public List<Nurse> getAllNurses() {
        return nurseService.getAllNurses();
    }

    // Get a nurse by ID
    @GetMapping("/{id}")
    public ResponseEntity<Nurse> getNurseById(@PathVariable Long id) {
        return nurseService.getNurseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a nurse
    @PutMapping("/{id}")
    public ResponseEntity<Nurse> updateNurse(@PathVariable Long id, @RequestBody Nurse nurseDetails) {
        Nurse updatedNurse = nurseService.updateNurse(id, nurseDetails);
        return ResponseEntity.ok(updatedNurse);
    }

    // Delete a nurse
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNurse(@PathVariable Long id) {
        nurseService.deleteNurse(id);
        return ResponseEntity.noContent().build();
    }
}