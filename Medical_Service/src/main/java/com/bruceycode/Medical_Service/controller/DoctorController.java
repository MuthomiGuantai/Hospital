package com.bruceycode.Medical_Service.controller;
import com.bruceycode.Medical_Service.model.Doctor;
import com.bruceycode.Medical_Service.model.Patient;
import com.bruceycode.Medical_Service.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Create a new doctor
    @PostMapping("/register")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }

    // Get a doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        return doctor.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get all doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    // Update a doctor
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id,
                                               @RequestBody Doctor doctorDetails) {
        try {
            Doctor updatedDoctor = doctorService.updateDoctor(id, doctorDetails);
            return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a doctor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{doctorId}/patients")
    public ResponseEntity<Doctor> addPatientToDoctor(
            @PathVariable Long doctorId,
            @RequestBody Patient patient) {
        Doctor updatedDoctor = doctorService.addPatientToDoctor(doctorId, patient.getPatientId());
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }
}