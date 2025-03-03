package com.bruceycode.Patient_Service.service.helper;

import com.bruceycode.Patient_Service.model.Appointments;
import com.bruceycode.Patient_Service.repository.AppointmentsRepository;
import com.bruceycode.Patient_Service.service.AppointmentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentsServiceImpl implements AppointmentsService {

    private final AppointmentsRepository appointmentsRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    private String getMedicalServiceUrl() {
        log.debug("Resolving MEDICAL_SERVICE URI");
        String url = discoveryClient.getInstances("medical_service").get(0).getUri().toString();
        log.info("Resolved MEDICAL_SERVICE URI: {}", url);
        return url;
    }

    private void validatePatientExists(Long patientId) {
        log.info("Validating patient ID: {}", patientId);
        try {
            restTemplate.getForObject(getMedicalServiceUrl() + "/patients/" + patientId, Object.class);
            log.debug("Patient ID {} validated successfully", patientId);
        } catch (Exception e) {
            log.error("Failed to validate patient ID {}: {}", patientId, e.getMessage());
            throw e; // Re-throw to maintain existing behavior
        }
    }

    private void validateDoctorExists(Long doctorId) {
        if (doctorId != null) {
            log.info("Validating doctor ID: {}", doctorId);
            try {
                restTemplate.getForObject(getMedicalServiceUrl() + "/doctors/" + doctorId, Object.class);
                log.debug("Doctor ID {} validated successfully", doctorId);
            } catch (Exception e) {
                log.error("Failed to validate doctor ID {}: {}", doctorId, e.getMessage());
                throw e; // Re-throw to maintain existing behavior
            }
        } else {
            log.debug("Doctor ID is null, skipping validation");
        }
    }

    @Override
    public Appointments createAppointments(Appointments appointment) {
        log.info("Creating appointment: {}", appointment);
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
            appointment.setReason(appointmentDetails.getReason());
            appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
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