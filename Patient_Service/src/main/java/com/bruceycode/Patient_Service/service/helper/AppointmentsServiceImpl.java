package com.bruceycode.Patient_Service.service.helper;

import com.bruceycode.Patient_Service.model.Appointments;
import com.bruceycode.Patient_Service.repository.AppointmentsRepository;
import com.bruceycode.Patient_Service.service.AppointmentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentsServiceImpl implements AppointmentsService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentsServiceImpl.class);

    private final AppointmentsRepository appointmentsRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    @Autowired
    public AppointmentsServiceImpl(AppointmentsRepository appointmentsRepository, RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.appointmentsRepository = appointmentsRepository;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
        logger.info("AppointmentsServiceImpl initialized with AppointmentsRepository, RestTemplate, and DiscoveryClient");
    }

    private String getMedicalServiceUrl() {
        logger.debug("Resolving MEDICAL_SERVICE URI");
        String url = discoveryClient.getInstances("medical_service").get(0).getUri().toString();
        logger.info("Resolved MEDICAL_SERVICE URI: {}", url);
        return url;
    }

    private void validatePatientExists(Long patientId) {
        logger.info("Validating patient ID: {}", patientId);
        try {
            restTemplate.getForObject(getMedicalServiceUrl() + "/patients/" + patientId, Object.class);
            logger.debug("Patient ID {} validated successfully", patientId);
        } catch (Exception e) {
            logger.error("Failed to validate patient ID {}: {}", patientId, e.getMessage());
            throw e; // Re-throw to maintain existing behavior
        }
    }

    private void validateDoctorExists(Long doctorId) {
        if (doctorId != null) {
            logger.info("Validating doctor ID: {}", doctorId);
            try {
                restTemplate.getForObject(getMedicalServiceUrl() + "/doctors/" + doctorId, Object.class);
                logger.debug("Doctor ID {} validated successfully", doctorId);
            } catch (Exception e) {
                logger.error("Failed to validate doctor ID {}: {}", doctorId, e.getMessage());
                throw e; // Re-throw to maintain existing behavior
            }
        } else {
            logger.debug("Doctor ID is null, skipping validation");
        }
    }

    @Override
    public Appointments createAppointments(Appointments appointment) {
        logger.info("Creating appointment: {}", appointment);
        validatePatientExists(appointment.getPatientId());
        validateDoctorExists(appointment.getDoctorId());
        Appointments savedAppointment = appointmentsRepository.save(appointment);
        logger.info("Successfully created appointment: {}", savedAppointment);
        return savedAppointment;
    }

    @Override
    public Optional<Appointments> getAppointmentById(Long id) {
        logger.info("Fetching appointment by ID: {}", id);
        Optional<Appointments> appointment = appointmentsRepository.findById(id);
        if (appointment.isPresent()) {
            logger.info("Successfully fetched appointment ID {}: {}", id, appointment.get());
        } else {
            logger.warn("No appointment found for ID: {}", id);
        }
        return appointment;
    }

    @Override
    public List<Appointments> getAllAppointments() {
        logger.info("Fetching all appointments");
        List<Appointments> appointments = appointmentsRepository.findAll();
        logger.info("Successfully fetched {} appointments", appointments.size());
        return appointments;
    }

    @Override
    public List<Appointments> getAppointmentsByPatientId(Long patientId) {
        logger.info("Fetching appointments for patient ID: {}", patientId);
        validatePatientExists(patientId);
        List<Appointments> appointments = appointmentsRepository.findByPatientId(patientId);
        logger.info("Successfully fetched {} appointments for patient ID: {}", appointments.size(), patientId);
        return appointments;
    }

    @Override
    public Appointments updateAppointment(Long id, Appointments appointmentDetails) {
        logger.info("Updating appointment ID {} with details: {}", id, appointmentDetails);
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
            logger.info("Successfully updated appointment ID {}: {}", id, updatedAppointment);
            return updatedAppointment;
        }
        logger.error("Appointment not found for update with ID: {}", id);
        throw new RuntimeException("Appointment not found with ID " + id);
    }

    @Override
    public void deleteAppointment(Long id) {
        logger.info("Deleting appointment with ID: {}", id);
        if (appointmentsRepository.existsById(id)) {
            appointmentsRepository.deleteById(id);
            logger.info("Successfully deleted appointment with ID: {}", id);
        } else {
            logger.error("Appointment not found for deletion with ID: {}", id);
            throw new RuntimeException("Appointment not found with ID " + id);
        }
    }
}