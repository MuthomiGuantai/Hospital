package com.bruceycode.Patient_Service.controller;

import com.bruceycode.Patient_Service.model.Admissions;
import com.bruceycode.Patient_Service.service.AdmissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admissions")
public class AdmissionsController {

    private final AdmissionsService admissionsService;

    @Autowired
    public AdmissionsController(AdmissionsService admissionsService) {
        this.admissionsService = admissionsService;
    }

    @PostMapping
    public ResponseEntity<Admissions> createAdmission(@RequestBody Admissions admission) {
        return new ResponseEntity<>(admissionsService.createAdmission(admission), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admissions> getAdmissionById(@PathVariable Long id) {
        return admissionsService.getAdmissionById(id)
                .map(admission -> new ResponseEntity<>(admission, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Admissions>> getAllAdmissions() {
        return new ResponseEntity<>(admissionsService.getAllAdmissions(), HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Admissions>> getAdmissionsByPatientId(@PathVariable Long patientId) {
        return new ResponseEntity<>(admissionsService.getAdmissionByPatientId(patientId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admissions> updateAdmission(@PathVariable Long id, @RequestBody Admissions admission) {
        try {
            return new ResponseEntity<>(admissionsService.updateAdmission(id, admission), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmission(@PathVariable Long id) {
        try {
            admissionsService.deleteAdmission(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}