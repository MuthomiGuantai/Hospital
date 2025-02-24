package com.bruceycode.Patient_Service.service.helper;

import com.bruceycode.Patient_Service.model.Appointments;
import com.bruceycode.Patient_Service.repository.AppointmentsRepository;
import com.bruceycode.Patient_Service.service.AppointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentsServiceImpl implements AppointmentsService {

    private final AppointmentsRepository appointmentsRepository;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    @Autowired
    public AppointmentsServiceImpl(AppointmentsRepository appointmentsRepository, RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.appointmentsRepository = appointmentsRepository;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    private String getMedicalServiceUrl() {
        return discoveryClient.getInstances("medical_service").get(0).getUri().toString();
    }

    private void validatePatientExists(Long patientId) {
        restTemplate.getForObject(getMedicalServiceUrl() + "/patients/" + patientId, Object.class);
    }

    private void validateDoctorExists(Long doctorId) {
        if (doctorId != null) {
            restTemplate.getForObject(getMedicalServiceUrl() + "/doctors/" + doctorId, Object.class);
        }
    }

    @Override
    public Appointments createAppointments(Appointments appointment) {
        validatePatientExists(appointment.getPatientId());
        validateDoctorExists(appointment.getDoctorId());
        return appointmentsRepository.save(appointment);
    }

    @Override
    public Optional<Appointments> getAppointmentById(Long id) {
        return appointmentsRepository.findById(id);
    }

    @Override
    public List<Appointments> getAllAppointments() {
        return appointmentsRepository.findAll();
    }

    @Override
    public List<Appointments> getAppointmentsByPatientId(Long patientId) {
        validatePatientExists(patientId);
        return appointmentsRepository.findByPatientId(patientId);
    }

    @Override
    public Appointments updateAppointment(Long id, Appointments appointmentDetails) {
        Optional<Appointments> optionalAppointment = appointmentsRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            Appointments appointment = optionalAppointment.get();
            validatePatientExists(appointmentDetails.getPatientId());
            validateDoctorExists(appointmentDetails.getDoctorId());
            appointment.setPatientId(appointmentDetails.getPatientId());
            appointment.setDoctorId(appointmentDetails.getDoctorId());
            appointment.setReason(appointmentDetails.getReason());
            appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
            return appointmentsRepository.save(appointment);
        }
        throw new RuntimeException("Appointment not found with ID " + id);
    }

    @Override
    public void deleteAppointment(Long id) {
        if (appointmentsRepository.existsById(id)) {
            appointmentsRepository.deleteById(id);
        } else {
            throw new RuntimeException("Appointment not found with ID " + id);
        }
    }
}