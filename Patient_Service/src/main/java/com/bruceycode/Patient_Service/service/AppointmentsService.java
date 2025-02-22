package com.bruceycode.Patient_Service.service;

import com.bruceycode.Patient_Service.model.Appointments;

import java.util.List;
import java.util.Optional;

public interface AppointmentsService {
    Appointments createAppointments(Appointments appointments);
    Optional<Appointments> getAppointmentById(Long id);
    List<Appointments> getAllAppointments();
    List<Appointments> getAppointmentsByPatientId(Long patientId);
    Appointments updateAppointment(Long id, Appointments appointments);
    void deleteAppointment(Long id);
}
