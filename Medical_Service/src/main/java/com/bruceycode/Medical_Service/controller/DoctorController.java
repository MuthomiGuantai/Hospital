package com.bruceycode.Medical_Service.controller;

import com.bruceycode.Medical_Service.dto.medical_services.DoctorDTO;
import com.bruceycode.Medical_Service.dto.patient_services.AdmissionsDTO;
import com.bruceycode.Medical_Service.dto.patient_services.AppointmentsDTO;
import com.bruceycode.Medical_Service.dto.patient_services.MedicalRecordDTO;
import com.bruceycode.Medical_Service.dto.patient_services.PatientDataDTO;
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

    // --- Medical Records ---
    @PostMapping("/{doctorId}/patients/{patientId}/medical-records")
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(
            @PathVariable Long doctorId, @PathVariable Long patientId, @RequestBody MedicalRecordDTO dto) {
        MedicalRecordDTO created = doctorService.createMedicalRecordForPatient(doctorId, patientId, dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{doctorId}/patients/{patientId}/medical-records/{recordId}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecord(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long recordId) {
        MedicalRecordDTO record = doctorService.getMedicalRecordById(doctorId, patientId, recordId);
        return ResponseEntity.ok(record);
    }

    @PutMapping("/{doctorId}/patients/{patientId}/medical-records/{recordId}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long recordId,
            @RequestBody MedicalRecordDTO dto) {
        MedicalRecordDTO updated = doctorService.updateMedicalRecord(doctorId, patientId, recordId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{doctorId}/patients/{patientId}/medical-records/{recordId}")
    public ResponseEntity<Void> deleteMedicalRecord(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long recordId) {
        doctorService.deleteMedicalRecord(doctorId, patientId, recordId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{doctorId}/patients/{patientId}/medical-records")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecords(
            @PathVariable Long doctorId, @PathVariable Long patientId) {
        List<MedicalRecordDTO> records = doctorService.getMedicalRecordsByPatientId(doctorId, patientId);
        return ResponseEntity.ok(records);
    }

    // --- Admissions ---
    @PostMapping("/{doctorId}/patients/{patientId}/admissions")
    public ResponseEntity<AdmissionsDTO> createAdmission(
            @PathVariable Long doctorId, @PathVariable Long patientId, @RequestBody AdmissionsDTO dto) {
        AdmissionsDTO created = doctorService.createAdmissionForPatient(doctorId, patientId, dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{doctorId}/patients/{patientId}/admissions/{admissionId}")
    public ResponseEntity<AdmissionsDTO> getAdmission(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long admissionId) {
        AdmissionsDTO admission = doctorService.getAdmissionById(doctorId, patientId, admissionId);
        return ResponseEntity.ok(admission);
    }

    @PutMapping("/{doctorId}/patients/{patientId}/admissions/{admissionId}")
    public ResponseEntity<AdmissionsDTO> updateAdmission(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long admissionId,
            @RequestBody AdmissionsDTO dto) {
        AdmissionsDTO updated = doctorService.updateAdmission(doctorId, patientId, admissionId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{doctorId}/patients/{patientId}/admissions/{admissionId}")
    public ResponseEntity<Void> deleteAdmission(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long admissionId) {
        doctorService.deleteAdmission(doctorId, patientId, admissionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{doctorId}/patients/{patientId}/admissions")
    public ResponseEntity<List<AdmissionsDTO>> getAdmissions(
            @PathVariable Long doctorId, @PathVariable Long patientId) {
        List<AdmissionsDTO> admissions = doctorService.getAdmissionsByPatientId(doctorId, patientId);
        return ResponseEntity.ok(admissions);
    }

    // --- Appointments ---
    @PostMapping("/{doctorId}/patients/{patientId}/appointments")
    public ResponseEntity<AppointmentsDTO> createAppointment(
            @PathVariable Long doctorId, @PathVariable Long patientId, @RequestBody AppointmentsDTO dto) {
        AppointmentsDTO created = doctorService.createAppointmentForPatient(doctorId, patientId, dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{doctorId}/patients/{patientId}/appointments/{appointmentId}")
    public ResponseEntity<AppointmentsDTO> getAppointment(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long appointmentId) {
        AppointmentsDTO appointment = doctorService.getAppointmentById(doctorId, patientId, appointmentId);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{doctorId}/patients/{patientId}/appointments/{appointmentId}")
    public ResponseEntity<AppointmentsDTO> updateAppointment(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long appointmentId,
            @RequestBody AppointmentsDTO dto) {
        AppointmentsDTO updated = doctorService.updateAppointment(doctorId, patientId, appointmentId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{doctorId}/patients/{patientId}/appointments/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long appointmentId) {
        doctorService.deleteAppointment(doctorId, patientId, appointmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{doctorId}/patients/{patientId}/appointments")
    public ResponseEntity<List<AppointmentsDTO>> getAppointments(
            @PathVariable Long doctorId, @PathVariable Long patientId) {
        List<AppointmentsDTO> appointments = doctorService.getAppointmentsByPatientId(doctorId, patientId);
        return ResponseEntity.ok(appointments);
    }
}