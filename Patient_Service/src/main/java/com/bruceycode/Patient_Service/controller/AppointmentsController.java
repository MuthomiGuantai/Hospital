package com.bruceycode.Patient_Service.controller;

import com.bruceycode.Patient_Service.model.Appointments;
import com.bruceycode.Patient_Service.service.AppointmentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentsController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentsController.class);

    private final AppointmentsService appointmentsService;

    @Autowired
    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
        logger.info("AppointmentsController initialized with AppointmentsService");
    }

    @PostMapping
    public ResponseEntity<Appointments> createAppointments(@RequestBody Appointments appointment) {
        logger.info("Received POST request to create appointment: {}", appointment);
        Appointments createdAppointment = appointmentsService.createAppointments(appointment);
        logger.info("Successfully created appointment: {}", createdAppointment);
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointments> getAppointmentById(@PathVariable Long id) {
        logger.info("Received GET request for appointment ID: {}", id);
        return appointmentsService.getAppointmentById(id)
                .map(appointment -> {
                    logger.info("Successfully retrieved appointment ID {}: {}", id, appointment);
                    return new ResponseEntity<>(appointment, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    logger.warn("Appointment not found for ID: {}", id);
                    throw new RuntimeException("Appointment not found for ID " + id);
                });
    }

    @GetMapping
    public ResponseEntity<List<Appointments>> getAllAppointments() {
        logger.info("Received GET request for all appointments");
        List<Appointments> appointments = appointmentsService.getAllAppointments();
        logger.info("Successfully retrieved {} appointments", appointments.size());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointments>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        logger.info("Received GET request for appointments of patient ID: {}", patientId);
        List<Appointments> appointments = appointmentsService.getAppointmentsByPatientId(patientId);
        logger.info("Successfully retrieved {} appointments for patient ID: {}", appointments.size(), patientId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointments> updateAppointment(@PathVariable Long id, @RequestBody Appointments appointment) {
        logger.info("Received PUT request to update appointment ID {} with details: {}", id, appointment);
        Appointments updatedAppointment = appointmentsService.updateAppointment(id, appointment);
        logger.info("Successfully updated appointment ID {}: {}", id, updatedAppointment);
        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        logger.info("Received DELETE request for appointment ID: {}", id);
        appointmentsService.deleteAppointment(id);
        logger.info("Successfully deleted appointment ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}