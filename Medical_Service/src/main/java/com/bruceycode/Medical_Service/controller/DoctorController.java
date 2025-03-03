package com.bruceycode.Medical_Service.controller;

import com.bruceycode.Medical_Service.model.entity.Doctor;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
        logger.info("DoctorController initialized with DoctorService");
    }



    @PostMapping("/register")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        logger.info("Received POST request to create doctor: {}", doctor);
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be null or empty");
        }
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        logger.info("Successfully created doctor: {}", createdDoctor);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        logger.info("Received GET request for doctor ID: {}", id);
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        if (doctor.isPresent()) {
            logger.info("Successfully retrieved doctor ID {}: {}", id, doctor.get());
            return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
        } else {
            logger.warn("Doctor not found for ID: {}", id);
            throw new RuntimeException("Doctor not found for ID: " + id);
        }
    }


    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        logger.info("Received GET request for all doctors");
        List<Doctor> doctors = doctorService.getAllDoctors();
        logger.info("Successfully retrieved {} doctors", doctors.size());
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id,
                                               @RequestBody Doctor doctorDetails) {
        logger.info("Received PUT request to update doctor ID {} with details: {}", id, doctorDetails);
        Doctor updatedDoctor = doctorService.updateDoctor(id, doctorDetails);
        logger.info("Successfully updated doctor ID {}: {}", id, updatedDoctor);
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        logger.info("Received DELETE request for doctor ID: {}", id);
        doctorService.deleteDoctor(id);
        logger.info("Successfully deleted doctor ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{doctorId}/patients")
    public ResponseEntity<Doctor> addPatientToDoctor(
            @PathVariable Long doctorId,
            @RequestBody Patient patient) {
        logger.info("Received POST request to add patient ID {} to doctor ID: {}", patient.getPatientId(), doctorId);
        Doctor updatedDoctor = doctorService.addPatientToDoctor(doctorId, patient.getPatientId());
        logger.info("Successfully added patient ID {} to doctor ID {}: {}", patient.getPatientId(), doctorId, updatedDoctor);
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }
}