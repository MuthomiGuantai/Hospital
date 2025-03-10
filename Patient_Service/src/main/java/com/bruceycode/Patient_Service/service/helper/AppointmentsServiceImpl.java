package com.bruceycode.Patient_Service.service.helper;

import com.bruceycode.Patient_Service.model.Appointments;
import com.bruceycode.Patient_Service.repository.AppointmentsRepository;
import com.bruceycode.Patient_Service.service.AppointmentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentsServiceImpl implements AppointmentsService {

    private final AppointmentsRepository appointmentsRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;
    private final HttpServletRequest request; // Add this for JWT

    private String getMedicalServiceUrl() {
        log.debug("Resolving MEDICAL_SERVICE URI");
        List<ServiceInstance> instances = discoveryClient.getInstances("medical_service");
        if (instances.isEmpty()) {
            log.error("No instances of 'medical_service' found in discovery server");
            throw new RuntimeException("Medical_Service not available");
        }
        String url = instances.get(0).getUri().toString();
        log.info("Resolved MEDICAL_SERVICE URI: {}", url);
        return url;
    }

    private HttpHeaders getAuthHeaders() {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            log.error("No valid Authorization header found");
            throw new RuntimeException("JWT token missing");
        }
        String jwtToken = header.substring(7);
        log.debug("Using JWT token: {}", jwtToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        return headers;
    }

    private void validatePatientExists(Long patientId) {
        if (patientId == null) {
            log.error("Patient ID is null during validation");
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        log.info("Validating patient ID: {}", patientId);
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
            restTemplate.exchange(
                    getMedicalServiceUrl() + "/patients/" + patientId, HttpMethod.GET, requestEntity, Object.class);
            log.debug("Patient ID {} validated successfully", patientId);
        } catch (Exception e) {
            log.error("Failed to validate patient ID {}: {}", patientId, e.getMessage());
            throw e;
        }
    }

    private void validateDoctorExists(Long doctorId) {
        if (doctorId != null) {
            log.info("Validating doctor ID: {}", doctorId);
            try {
                HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
                restTemplate.exchange(
                        getMedicalServiceUrl() + "/doctors/" + doctorId, HttpMethod.GET, requestEntity, Object.class);
                log.debug("Doctor ID {} validated successfully", doctorId);
            } catch (Exception e) {
                log.error("Failed to validate doctor ID {}: {}", doctorId, e.getMessage());
                throw e;
            }
        } else {
            log.debug("Doctor ID is null, skipping validation");
        }
    }

    @Override
    public Appointments createAppointments(Appointments appointment) {
        log.info("Creating appointment: {}", appointment);
        if (appointment.getPatientId() == null) {
            log.error("Patient ID is null in appointment: {}", appointment);
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        validatePatientExists(appointment.getPatientId());
        validateDoctorExists(appointment.getDoctorId());
        Appointments savedAppointment = appointmentsRepository.save(appointment);
        log.info("Successfully created appointment: {}", savedAppointment);
        return savedAppointment;
    }

    @Override
    public Optional<Appointments> getAppointmentById(Long id) {
        log.info("Fetching appointment by ID: {}", id);
        Optional<Appointments> appointment = appointmentsRepository.findById(id);
        if (appointment.isPresent()) {
            log.info("Successfully fetched appointment ID {}: {}", id, appointment.get());
        } else {
            log.warn("No appointment found for ID: {}", id);
        }
        return appointment;
    }

    @Override
    public List<Appointments> getAllAppointments() {
        log.info("Fetching all appointments");
        List<Appointments> appointments = appointmentsRepository.findAll();
        log.info("Successfully fetched {} appointments", appointments.size());
        return appointments;
    }

    @Override
    public List<Appointments> getAppointmentsByPatientId(Long patientId) {
        log.info("Fetching appointments for patient ID: {}", patientId);
        validatePatientExists(patientId);
        List<Appointments> appointments = appointmentsRepository.findByPatientId(patientId);
        log.info("Successfully fetched {} appointments for patient ID: {}", appointments.size(), patientId);
        return appointments;
    }

    @Override
    public Appointments updateAppointment(Long id, Appointments appointmentDetails) {
        log.info("Updating appointment ID {} with details: {}", id, appointmentDetails);
        Optional<Appointments> optionalAppointment = appointmentsRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            Appointments appointment = optionalAppointment.get();
            validatePatientExists(appointmentDetails.getPatientId());
            validateDoctorExists(appointmentDetails.getDoctorId());
            appointment.setPatientId(appointmentDetails.getPatientId());
            appointment.setDoctorId(appointmentDetails.getDoctorId());
            appointment.setNurseId(appointmentDetails.getNurseId());
            appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
            appointment.setReason(appointmentDetails.getReason());
            Appointments updatedAppointment = appointmentsRepository.save(appointment);
            log.info("Successfully updated appointment ID {}: {}", id, updatedAppointment);
            return updatedAppointment;
        }
        log.error("Appointment not found for update with ID: {}", id);
        throw new RuntimeException("Appointment not found with ID " + id);
    }

    @Override
    public void deleteAppointment(Long id) {
        log.info("Deleting appointment with ID: {}", id);
        if (appointmentsRepository.existsById(id)) {
            appointmentsRepository.deleteById(id);
            log.info("Successfully deleted appointment with ID: {}", id);
        } else {
            log.error("Appointment not found for deletion with ID: {}", id);
            throw new RuntimeException("Appointment not found with ID " + id);
        }
    }
}