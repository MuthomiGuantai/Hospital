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

    // Medical Record Endpoints
    @PostMapping("/{doctorId}/patients/{patientId}/medical-records")
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(
            @PathVariable Long doctorId, @PathVariable Long patientId, @RequestBody MedicalRecordDTO medicalRecordDTO) {
        log.info("Received POST request to create medical record for patient ID {} by doctor ID {}", patientId, doctorId);
        MedicalRecordDTO createdRecord = doctorService.createMedicalRecordForPatient(doctorId, patientId, medicalRecordDTO);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }

    @GetMapping("/{doctorId}/patients/{patientId}/medical-records/{recordId}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordById(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long recordId) {
        log.info("Received GET request for medical record ID {} for patient ID {} by doctor ID {}", recordId, patientId, doctorId);
        MedicalRecordDTO record = doctorService.getMedicalRecordById(doctorId, patientId, recordId);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @GetMapping("/{doctorId}/patients/{patientId}/medical-records")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByPatientId(
            @PathVariable Long doctorId, @PathVariable Long patientId) {
        log.info("Received GET request for medical records for patient ID {} by doctor ID {}", patientId, doctorId);
        List<MedicalRecordDTO> records = doctorService.getMedicalRecordsByPatientId(doctorId, patientId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PutMapping("/{doctorId}/patients/{patientId}/medical-records/{recordId}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long recordId,
            @RequestBody MedicalRecordDTO medicalRecordDTO) {
        log.info("Received PUT request to update medical record ID {} for patient ID {} by doctor ID {}", recordId, patientId, doctorId);
        MedicalRecordDTO updatedRecord = doctorService.updateMedicalRecord(doctorId, patientId, recordId, medicalRecordDTO);
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    @DeleteMapping("/{doctorId}/patients/{patientId}/medical-records/{recordId}")
    public ResponseEntity<Void> deleteMedicalRecord(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long recordId) {
        log.info("Received DELETE request for medical record ID {} for patient ID {} by doctor ID {}", recordId, patientId, doctorId);
        doctorService.deleteMedicalRecord(doctorId, patientId, recordId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Admissions Endpoints
    @PostMapping("/{doctorId}/patients/{patientId}/admissions")
    public ResponseEntity<AdmissionsDTO> createAdmission(
            @PathVariable Long doctorId, @PathVariable Long patientId, @RequestBody AdmissionsDTO admissionsDTO) {
        log.info("Received POST request to create admission for patient ID {} by doctor ID {}", patientId, doctorId);
        AdmissionsDTO createdAdmission = doctorService.createAdmissionForPatient(doctorId, patientId, admissionsDTO);
        return new ResponseEntity<>(createdAdmission, HttpStatus.CREATED);
    }

    @GetMapping("/{doctorId}/patients/{patientId}/admissions/{admissionId}")
    public ResponseEntity<AdmissionsDTO> getAdmissionById(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long admissionId) {
        log.info("Received GET request for admission ID {} for patient ID {} by doctor ID {}", admissionId, patientId, doctorId);
        AdmissionsDTO admission = doctorService.getAdmissionById(doctorId, patientId, admissionId);
        return new ResponseEntity<>(admission, HttpStatus.OK);
    }

    @GetMapping("/{doctorId}/patients/{patientId}/admissions")
    public ResponseEntity<List<AdmissionsDTO>> getAdmissionsByPatientId(
            @PathVariable Long doctorId, @PathVariable Long patientId) {
        log.info("Received GET request for admissions for patient ID {} by doctor ID {}", patientId, doctorId);
        List<AdmissionsDTO> admissions = doctorService.getAdmissionsByPatientId(doctorId, patientId);
        return new ResponseEntity<>(admissions, HttpStatus.OK);
    }

    @PutMapping("/{doctorId}/patients/{patientId}/admissions/{admissionId}")
    public ResponseEntity<AdmissionsDTO> updateAdmission(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long admissionId,
            @RequestBody AdmissionsDTO admissionsDTO) {
        log.info("Received PUT request to update admission ID {} for patient ID {} by doctor ID {}", admissionId, patientId, doctorId);
        AdmissionsDTO updatedAdmission = doctorService.updateAdmission(doctorId, patientId, admissionId, admissionsDTO);
        return new ResponseEntity<>(updatedAdmission, HttpStatus.OK);
    }

    @DeleteMapping("/{doctorId}/patients/{patientId}/admissions/{admissionId}")
    public ResponseEntity<Void> deleteAdmission(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long admissionId) {
        log.info("Received DELETE request for admission ID {} for patient ID {} by doctor ID {}", admissionId, patientId, doctorId);
        doctorService.deleteAdmission(doctorId, patientId, admissionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Appointments Endpoints
    @PostMapping("/{doctorId}/patients/{patientId}/appointments")
    public ResponseEntity<AppointmentsDTO> createAppointment(
            @PathVariable Long doctorId, @PathVariable Long patientId, @RequestBody AppointmentsDTO appointmentsDTO) {
        log.info("Received POST request to create appointment for patient ID {} by doctor ID {}", patientId, doctorId);
        AppointmentsDTO createdAppointment = doctorService.createAppointmentForPatient(doctorId, patientId, appointmentsDTO);
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    @GetMapping("/{doctorId}/patients/{patientId}/appointments/{appointmentId}")
    public ResponseEntity<AppointmentsDTO> getAppointmentById(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long appointmentId) {
        log.info("Received GET request for appointment ID {} for patient ID {} by doctor ID {}", appointmentId, patientId, doctorId);
        AppointmentsDTO appointment = doctorService.getAppointmentById(doctorId, patientId, appointmentId);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/{doctorId}/patients/{patientId}/appointments")
    public ResponseEntity<List<AppointmentsDTO>> getAppointmentsByPatientId(
            @PathVariable Long doctorId, @PathVariable Long patientId) {
        log.info("Received GET request for appointments for patient ID {} by doctor ID {}", patientId, doctorId);
        List<AppointmentsDTO> appointments = doctorService.getAppointmentsByPatientId(doctorId, patientId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PutMapping("/{doctorId}/patients/{patientId}/appointments/{appointmentId}")
    public ResponseEntity<AppointmentsDTO> updateAppointment(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long appointmentId,
            @RequestBody AppointmentsDTO appointmentsDTO) {
        log.info("Received PUT request to update appointment ID {} for patient ID {} by doctor ID {}", appointmentId, patientId, doctorId);
        AppointmentsDTO updatedAppointment = doctorService.updateAppointment(doctorId, patientId, appointmentId, appointmentsDTO);
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }

    @DeleteMapping("/{doctorId}/patients/{patientId}/appointments/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long doctorId, @PathVariable Long patientId, @PathVariable Long appointmentId) {
        log.info("Received DELETE request for appointment ID {} for patient ID {} by doctor ID {}", appointmentId, patientId, doctorId);
        doctorService.deleteAppointment(doctorId, patientId, appointmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Patient Data Endpoint
    @GetMapping("/{doctorId}/patients/{patientId}/data")
    public ResponseEntity<PatientDataDTO> getPatientData(
            @PathVariable Long doctorId, @PathVariable Long patientId) {
        log.info("Received GET request for patient data for patient ID {} by doctor ID {}", patientId, doctorId);
        PatientDataDTO patientData = doctorService.getPatientData(doctorId, patientId);
        return new ResponseEntity<>(patientData, HttpStatus.OK);
    }

}