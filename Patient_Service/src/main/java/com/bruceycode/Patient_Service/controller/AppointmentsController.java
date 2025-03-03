package com.bruceycode.Patient_Service.controller;

import com.bruceycode.Patient_Service.model.Appointments;
import com.bruceycode.Patient_Service.service.AppointmentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/appointments")
public class AppointmentsController {

    private final AppointmentsService appointmentsService;

    @PostMapping
    public ResponseEntity<Appointments> createAppointments(@RequestBody Appointments appointment) {
        log.info("Received POST request to create appointment: {}", appointment);
        Appointments createdAppointment = appointmentsService.createAppointments(appointment);
        log.info("Successfully created appointment: {}", createdAppointment);
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointments> getAppointmentById(@PathVariable Long id) {
        log.info("Received GET request for appointment ID: {}", id);
        return appointmentsService.getAppointmentById(id)
                .map(appointment -> {
                    log.info("Successfully retrieved appointment ID {}: {}", id, appointment);
                    return new ResponseEntity<>(appointment, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    log.warn("Appointment not found for ID: {}", id);
                    throw new RuntimeException("Appointment not found for ID " + id);
                });
    }

    @GetMapping
    public ResponseEntity<List<Appointments>> getAllAppointments() {
        log.info("Received GET request for all appointments");
        List<Appointments> appointments = appointmentsService.getAllAppointments();
        log.info("Successfully retrieved {} appointments", appointments.size());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointments>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        log.info("Received GET request for appointments of patient ID: {}", patientId);
        List<Appointments> appointments = appointmentsService.getAppointmentsByPatientId(patientId);
        log.info("Successfully retrieved {} appointments for patient ID: {}", appointments.size(), patientId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointments> updateAppointment(@PathVariable Long id, @RequestBody Appointments appointment) {
        log.info("Received PUT request to update appointment ID {} with details: {}", id, appointment);
        Appointments updatedAppointment = appointmentsService.updateAppointment(id, appointment);
        log.info("Successfully updated appointment ID {}: {}", id, updatedAppointment);
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        log.info("Received DELETE request for appointment ID: {}", id);
        appointmentsService.deleteAppointment(id);
        log.info("Successfully deleted appointment ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}