package com.bruceycode.Patient_Service.controller;

import com.bruceycode.Patient_Service.dto.AppointmentsDTO;
import com.bruceycode.Patient_Service.model.Appointments;
import com.bruceycode.Patient_Service.service.AppointmentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/appointments")
public class AppointmentsController {

    private final AppointmentsService appointmentsService;

    @PostMapping
    public ResponseEntity<AppointmentsDTO> createAppointments(@RequestBody AppointmentsDTO appointmentDTO) {
        log.info("Received POST request to create appointment: {}", appointmentDTO);
        Appointments createdAppointment = appointmentsService.createAppointments(toEntity(appointmentDTO));
        log.info("Successfully created appointment: {}", createdAppointment);
        return new ResponseEntity<>(toDto(createdAppointment), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentsDTO> getAppointmentById(@PathVariable Long id) {
        log.info("Received GET request for appointment ID: {}", id);
        return appointmentsService.getAppointmentById(id)
                .map(appointment -> {
                    log.info("Successfully retrieved appointment ID {}: {}", id, appointment);
                    return new ResponseEntity<>(toDto(appointment), HttpStatus.OK);
                })
                .orElseGet(() -> {
                    log.warn("Appointment not found for ID: {}", id);
                    throw new RuntimeException("Appointment not found for ID " + id);
                });
    }

    @GetMapping
    public ResponseEntity<List<AppointmentsDTO>> getAllAppointments() {
        log.info("Received GET request for all appointments");
        List<Appointments> appointments = appointmentsService.getAllAppointments();
        log.info("Successfully retrieved {} appointments", appointments.size());
        return new ResponseEntity<>(appointments.stream().map(this::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentsDTO>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        log.info("Received GET request for appointments of patient ID: {}", patientId);
        List<Appointments> appointments = appointmentsService.getAppointmentsByPatientId(patientId);
        log.info("Successfully retrieved {} appointments for patient ID: {}", appointments.size(), patientId);
        return new ResponseEntity<>(appointments.stream().map(this::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentsDTO> updateAppointment(@PathVariable Long id, @RequestBody AppointmentsDTO appointmentDTO) {
        log.info("Received PUT request to update appointment ID {} with details: {}", id, appointmentDTO);
        Appointments updatedAppointment = appointmentsService.updateAppointment(id, toEntity(appointmentDTO));
        log.info("Successfully updated appointment ID {}: {}", id, updatedAppointment);
        return new ResponseEntity<>(toDto(updatedAppointment), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        log.info("Received DELETE request for appointment ID: {}", id);
        appointmentsService.deleteAppointment(id);
        log.info("Successfully deleted appointment ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Appointments toEntity(AppointmentsDTO dto) {
        Appointments entity = new Appointments();
        entity.setId(dto.getId());
        entity.setPatientId(dto.getPatientId());
        entity.setDoctorId(dto.getDoctorId());
        entity.setNurseId(dto.getNurseId());
        entity.setAppointmentDate(dto.getAppointmentDate());
        entity.setReason(dto.getReason());
        return entity;
    }

    private AppointmentsDTO toDto(Appointments entity) {
        AppointmentsDTO dto = new AppointmentsDTO();
        dto.setId(entity.getId());
        dto.setPatientId(entity.getPatientId());
        dto.setDoctorId(entity.getDoctorId());
        dto.setNurseId(entity.getNurseId());
        dto.setAppointmentDate(entity.getAppointmentDate());
        dto.setReason(entity.getReason());
        return dto;
    }
}