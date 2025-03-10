package com.bruceycode.Medical_Service.service;

import com.bruceycode.Medical_Service.dto.medical_services.DoctorDTO;
import com.bruceycode.Medical_Service.dto.medical_services.PatientDTO;
import com.bruceycode.Medical_Service.dto.patient_services.AdmissionsDTO;
import com.bruceycode.Medical_Service.dto.patient_services.AppointmentsDTO;
import com.bruceycode.Medical_Service.dto.patient_services.MedicalRecordDTO;
import com.bruceycode.Medical_Service.dto.patient_services.PatientDataDTO;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO doctorDTO);
    Optional<DoctorDTO> getDoctorById(Long id, boolean includePatients);
    List<DoctorDTO> getAllDoctors(); // Existing method
    List<DoctorDTO> getAllDoctors(boolean includePatients); // New method
    DoctorDTO updateDoctor(Long id, DoctorDTO doctorDetails);
    void deleteDoctor(Long id);
    DoctorDTO addPatientToDoctor(Long doctorId, Long patientId);

    // Doctor-Patient relationship operations
    List<PatientDTO> getPatientsByDoctorId(Long doctorId);

    // Medical Record operations for a patient
    MedicalRecordDTO createMedicalRecordForPatient(Long doctorId, Long patientId, MedicalRecordDTO medicalRecordDTO);
    MedicalRecordDTO getMedicalRecordById(Long doctorId, Long patientId, Long recordId);
    List<MedicalRecordDTO> getMedicalRecordsByPatientId(Long doctorId, Long patientId);
    MedicalRecordDTO updateMedicalRecord(Long doctorId, Long patientId, Long recordId, MedicalRecordDTO medicalRecordDTO);
    void deleteMedicalRecord(Long doctorId, Long patientId, Long recordId);

    // Admission operations for a patient
    AdmissionsDTO createAdmissionForPatient(Long doctorId, Long patientId, AdmissionsDTO admissionsDTO);
    AdmissionsDTO getAdmissionById(Long doctorId, Long patientId, Long admissionId);
    List<AdmissionsDTO> getAdmissionsByPatientId(Long doctorId, Long patientId);
    AdmissionsDTO updateAdmission(Long doctorId, Long patientId, Long admissionId, AdmissionsDTO admissionsDTO);
    void deleteAdmission(Long doctorId, Long patientId, Long admissionId);

    // Appointment operations for a patient
    AppointmentsDTO createAppointmentForPatient(Long doctorId, Long patientId, AppointmentsDTO appointmentsDTO);
    AppointmentsDTO getAppointmentById(Long doctorId, Long patientId, Long appointmentId);
    List<AppointmentsDTO> getAppointmentsByPatientId(Long doctorId, Long patientId);
    AppointmentsDTO updateAppointment(Long doctorId, Long patientId, Long appointmentId, AppointmentsDTO appointmentsDTO);
    void deleteAppointment(Long doctorId, Long patientId, Long appointmentId);

    // Consolidated patient data
    PatientDataDTO getPatientData(Long doctorId, Long patientId);
}