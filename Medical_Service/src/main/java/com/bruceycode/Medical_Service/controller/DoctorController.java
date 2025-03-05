package com.bruceycode.Medical_Service.controller;

import com.bruceycode.Medical_Service.dto.medical_services.DoctorDTO;
import com.bruceycode.Medical_Service.service.DoctorService;
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
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        log.info("Received POST request to create doctor: {}", doctorDTO);
        if (doctorDTO.getName() == null || doctorDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name cannot be null or empty");
        }
        DoctorDTO createdDoctor = doctorService.createDoctor(doctorDTO);
        log.info("Successfully created doctor: {}", createdDoctor);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        log.info("Received GET request for doctor ID: {}", id);
        Optional<DoctorDTO> doctor = doctorService.getDoctorById(id, true); // Always include patients
        if (doctor.isPresent()) {
            log.info("Successfully retrieved doctor ID {}: {}", id, doctor.get());
            return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
        } else {
            log.warn("Doctor not found for ID: {}", id);
            throw new RuntimeException("Doctor not found for ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        log.info("Received GET request for all doctors");
        List<DoctorDTO> doctors = doctorService.getAllDoctors(true); // Always include patients
        log.info("Successfully retrieved {} doctors", doctors.size());
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDetails) {
        log.info("Received PUT request to update doctor ID {} with details: {}", id, doctorDetails);
        DoctorDTO updatedDoctor = doctorService.updateDoctor(id, doctorDetails);
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

    @PostMapping("/{doctorId}/patients/{patientId}")
    public ResponseEntity<DoctorDTO> addPatientToDoctor(@PathVariable Long doctorId, @PathVariable Long patientId) {
        log.info("Received POST request to add patient ID {} to doctor ID: {}", patientId, doctorId);
        DoctorDTO updatedDoctor = doctorService.addPatientToDoctor(doctorId, patientId);
        log.info("Successfully added patient ID {} to doctor ID {}: {}", patientId, doctorId, updatedDoctor);
        return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
    }
}