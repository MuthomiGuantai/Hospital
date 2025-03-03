package com.bruceycode.Medical_Service.controller;

import com.bruceycode.Medical_Service.model.entity.Doctor;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.service.DoctorService;
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
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;


    @PostMapping("/register")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        log.info("Received POST request to create doctor: {}", doctor);
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be null or empty");
        }
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        log.info("Successfully created doctor: {}", createdDoctor);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        log.info("Received GET request for doctor ID: {}", id);
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        if (doctor.isPresent()) {
            log.info("Successfully retrieved doctor ID {}: {}", id, doctor.get());
            return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
        } else {
            log.warn("Doctor not found for ID: {}", id);
            throw new RuntimeException("Doctor not found for ID: " + id);
        }
    }


    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        log.info("Received GET request for all doctors");
        List<Doctor> doctors = doctorService.getAllDoctors();
        log.info("Successfully retrieved {} doctors", doctors.size());
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id,
                                               @RequestBody Doctor doctorDetails) {
        log.info("Received PUT request to update doctor ID {} with details: {}", id, doctorDetails);
        Doctor updatedDoctor = doctorService.updateDoctor(id, doctorDetails);
        log.info("Successfully updated doctor ID {}: {}", id, updatedDoctor);
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        log.info("Received DELETE request for doctor ID: {}", id);
        doctorService.deleteDoctor(id);
        log.info("Successfully deleted doctor ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{doctorId}/patients")
    public ResponseEntity<Doctor> addPatientToDoctor(
            @PathVariable Long doctorId,
            @RequestBody Patient patient) {
        log.info("Received POST request to add patient ID {} to doctor ID: {}", patient.getPatientId(), doctorId);
        Doctor updatedDoctor = doctorService.addPatientToDoctor(doctorId, patient.getPatientId());
        log.info("Successfully added patient ID {} to doctor ID {}: {}", patient.getPatientId(), doctorId, updatedDoctor);
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }
}