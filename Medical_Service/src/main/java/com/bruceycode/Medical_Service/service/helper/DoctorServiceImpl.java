package com.bruceycode.Medical_Service.service.helper;

import com.bruceycode.Medical_Service.dto.medical_services.DoctorDTO;
import com.bruceycode.Medical_Service.dto.medical_services.PatientDTO;
import com.bruceycode.Medical_Service.dto.patient_services.AdmissionsDTO;
import com.bruceycode.Medical_Service.dto.patient_services.AppointmentsDTO;
import com.bruceycode.Medical_Service.dto.patient_services.MedicalRecordDTO;
import com.bruceycode.Medical_Service.dto.patient_services.PatientDataDTO;
import com.bruceycode.Medical_Service.model.entity.Doctor;
import com.bruceycode.Medical_Service.model.entity.Nurse;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.repository.DoctorRepository;
import com.bruceycode.Medical_Service.repository.PatientRepository;
import com.bruceycode.Medical_Service.service.DoctorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        log.info("Creating doctor: {}", doctorDTO);
        Doctor doctor = toEntity(doctorDTO);
        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("Successfully created doctor: {}", savedDoctor);
        return toDto(savedDoctor);
    }

    @Override
    public Optional<DoctorDTO> getDoctorById(Long id, boolean includePatients) {
        log.info("Fetching doctor by ID: {}, includePatients: {}", id, includePatients);
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            log.info("Successfully fetched doctor ID {}: {}", id, doctor.get());
            DoctorDTO dto = toDto(doctor.get(), includePatients);
            log.debug("Returning DoctorDTO with patients included: {}", includePatients ? "yes" : "no");
            return Optional.of(dto);
        } else {
            log.warn("No doctor found for ID: {}", id);
            return Optional.empty();
        }
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        return getAllDoctors(false); // Default to not include patients for backward compatibility
    }

    @Override
    public List<DoctorDTO> getAllDoctors(boolean includePatients) {
        log.info("Fetching all doctors, includePatients: {}", includePatients);
        List<Doctor> doctors = doctorRepository.findAll();
        log.info("Successfully fetched {} doctors", doctors.size());
        return doctors.stream()
                .map(doctor -> toDto(doctor, includePatients))
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDTO updateDoctor(Long id, DoctorDTO doctorDetails) {
        log.info("Updating doctor ID {} with details: {}", id, doctorDetails);
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);

        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            doctor.setName(doctorDetails.getName());
            doctor.setUsername(doctorDetails.getUsername());
            doctor.setSpecialization(doctorDetails.getSpecialization());
            doctor.setDepartment(doctorDetails.getDepartment());
            doctor.setContactPhone(doctorDetails.getContactPhone());
            doctor.setContactEmail(doctorDetails.getContactEmail());
            doctor.setOfficeLocation(doctorDetails.getOfficeLocation());
            doctor.setSchedule(doctorDetails.getSchedule());
            if (doctorDetails.getPatientIds() != null) {
                List<Patient> patients = patientRepository.findAllById(doctorDetails.getPatientIds());
                if (patients.size() != doctorDetails.getPatientIds().size()) {
                    log.error("One or more patient IDs not found for doctor ID: {}", id);
                    throw new RuntimeException("One or more patient IDs not found");
                }
                doctor.setPatients(patients);
            }
            Doctor updatedDoctor = doctorRepository.save(doctor);
            log.info("Successfully updated doctor ID {}: {}", id, updatedDoctor);
            return toDto(updatedDoctor);
        }
        log.error("Doctor not found for update with ID: {}", id);
        throw new RuntimeException("Doctor not found with id " + id);
    }

    @Override
    public void deleteDoctor(Long id) {
        log.info("Deleting doctor with ID: {}", id);
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            doctorRepository.deleteById(id);
            log.info("Successfully deleted doctor with ID: {}", id);
        } else {
            log.error("Doctor not found for deletion with ID: {}", id);
            throw new RuntimeException("Doctor not found with id " + id);
        }
    }

    @Override
    public List<PatientDTO> getPatientsByDoctorId(Long doctorId) {
        log.info("Fetching patients for doctor ID: {}", doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> {
                    log.error("Doctor not found for ID: {}", doctorId);
                    return new RuntimeException("Doctor not found with ID " + doctorId);
                });
        List<PatientDTO> patients = doctor.getPatients().stream()
                .map(this::toPatientDto)
                .collect(Collectors.toList());
        log.info("Successfully fetched {} patients for doctor's ID: {}", patients.size(), doctorId);
        return patients;
    }

    @Override
    public DoctorDTO addPatientToDoctor(Long doctorId, Long patientId) {
        log.info("Adding patient ID {} to doctor ID: {}", patientId, doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> {
                    log.error("Doctor not found with ID: {}", doctorId);
                    return new RuntimeException("Doctor not found");
                });
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> {
                    log.error("Patient not found with ID: {}", patientId);
                    return new RuntimeException("Patient not found");
                });

        doctor.addPatient(patient);
        Doctor updatedDoctor = doctorRepository.save(doctor);
        log.info("Successfully added patient ID {} to doctor ID {}: {}", patientId, doctorId, updatedDoctor);
        return toDto(updatedDoctor);
    }

    private String getPatientServiceUrl() {
        log.debug("Resolving PATIENT_SERVICE URI");
        List<ServiceInstance> instances = discoveryClient.getInstances("patient_service");
        if (instances.isEmpty()) {
            log.error("No instances of 'patient_service' found in discovery server");
            throw new RuntimeException("Patient_Service not available");
        }
        String url = instances.get(0).getUri().toString();
        log.info("Resolved PATIENT_SERVICE URI: {}", url);
        return url;
    }

    private HttpHeaders getAuthHeaders() {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            log.error("No valid Authorization header found");
            throw new RuntimeException("JWT token missing");
        }
        String jwtToken = header.substring(7);
        log.debug("Forwarding JWT token: {}", jwtToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        return headers;
    }

    private void validateDoctorPatientRelationship(Long doctorId, Long patientId) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent() && !doctorOpt.get().getPatients().stream().anyMatch(p -> p.getPatientId().equals(patientId))) {
            log.error("Doctor ID {} is not associated with patient ID {}", doctorId, patientId);
            throw new RuntimeException("Doctor is not associated with this patient");
        }
    }

    @Override
    public MedicalRecordDTO createMedicalRecordForPatient(Long doctorId, Long patientId, MedicalRecordDTO medicalRecordDTO) {
        log.info("Creating medical record for patient ID {} by doctor ID {}", patientId, doctorId);
        if (patientId == null || doctorId == null || medicalRecordDTO == null) {
            log.error("Invalid input: patientId={}, doctorId={}, medicalRecordDTO={}", patientId, doctorId, medicalRecordDTO);
            throw new IllegalArgumentException("Patient ID, Doctor ID, or MedicalRecordDTO cannot be null");
        }
        validateDoctorPatientRelationship(doctorId, patientId);
        medicalRecordDTO.setId(null);
        medicalRecordDTO.setPatientId(patientId);
        medicalRecordDTO.setDoctorId(doctorId);
        HttpEntity<MedicalRecordDTO> requestEntity = new HttpEntity<>(medicalRecordDTO, getAuthHeaders());
        ResponseEntity<MedicalRecordDTO> response = restTemplate.postForEntity(
                getPatientServiceUrl() + "/medical-records", requestEntity, MedicalRecordDTO.class);
        log.info("Successfully created medical record: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public MedicalRecordDTO getMedicalRecordById(Long doctorId, Long patientId, Long recordId) {
        log.info("Fetching medical record ID {} for patient ID {} by doctor ID {}", recordId, patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<MedicalRecordDTO> response = restTemplate.exchange(
                getPatientServiceUrl() + "/medical-records/" + recordId, HttpMethod.GET, requestEntity, MedicalRecordDTO.class);
        return response.getBody();
    }

    @Override
    public List<MedicalRecordDTO> getMedicalRecordsByPatientId(Long doctorId, Long patientId) {
        log.info("Fetching medical records for patient ID {} by doctor ID {}", patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<MedicalRecordDTO[]> response = restTemplate.exchange(
                getPatientServiceUrl() + "/medical-records/patient/" + patientId, HttpMethod.GET, requestEntity, MedicalRecordDTO[].class);
        return Arrays.asList(response.getBody());
    }

    @Override
    public MedicalRecordDTO updateMedicalRecord(Long doctorId, Long patientId, Long recordId, MedicalRecordDTO medicalRecordDTO) {
        log.info("Updating medical record ID {} for patient ID {} by doctor ID {}", recordId, patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        HttpEntity<MedicalRecordDTO> requestEntity = new HttpEntity<>(medicalRecordDTO, getAuthHeaders());
        ResponseEntity<MedicalRecordDTO> response = restTemplate.exchange(
                getPatientServiceUrl() + "/medical-records/" + recordId, HttpMethod.PUT, requestEntity, MedicalRecordDTO.class);
        log.info("Successfully updated medical record: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public void deleteMedicalRecord(Long doctorId, Long patientId, Long recordId) {
        log.info("Deleting medical record ID {} for patient ID {} by doctor ID {}", recordId, patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
        restTemplate.exchange(
                getPatientServiceUrl() + "/medical-records/" + recordId, HttpMethod.DELETE, requestEntity, Void.class);
        log.info("Successfully deleted medical record ID {}", recordId);
    }

    @Override
    public AdmissionsDTO createAdmissionForPatient(Long doctorId, Long patientId, AdmissionsDTO admissionsDTO) {
        log.info("Creating admission for patient ID {} by doctor ID {}", patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        admissionsDTO.setId(null);
        admissionsDTO.setPatientId(patientId);
        HttpEntity<AdmissionsDTO> requestEntity = new HttpEntity<>(admissionsDTO, getAuthHeaders());
        ResponseEntity<AdmissionsDTO> response = restTemplate.postForEntity(
                getPatientServiceUrl() + "/admissions", requestEntity, AdmissionsDTO.class);
        log.info("Successfully created admission: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public AdmissionsDTO getAdmissionById(Long doctorId, Long patientId, Long admissionId) {
        log.info("Fetching admission ID {} for patient ID {} by doctor ID {}", admissionId, patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<AdmissionsDTO> response = restTemplate.exchange(
                getPatientServiceUrl() + "/admissions/" + admissionId, HttpMethod.GET, requestEntity, AdmissionsDTO.class);
        return response.getBody();
    }

    @Override
    public List<AdmissionsDTO> getAdmissionsByPatientId(Long doctorId, Long patientId) {
        log.info("Fetching admissions for patient ID {} by doctor ID {}", patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<AdmissionsDTO[]> response = restTemplate.exchange(
                getPatientServiceUrl() + "/admissions/patient/" + patientId, HttpMethod.GET, requestEntity, AdmissionsDTO[].class);
        return Arrays.asList(response.getBody());
    }

    @Override
    public AdmissionsDTO updateAdmission(Long doctorId, Long patientId, Long admissionId, AdmissionsDTO admissionsDTO) {
        log.info("Updating admission ID {} for patient ID {} by doctor ID {}", admissionId, patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        admissionsDTO.setPatientId(patientId);
        HttpEntity<AdmissionsDTO> requestEntity = new HttpEntity<>(admissionsDTO, getAuthHeaders());
        ResponseEntity<AdmissionsDTO> response = restTemplate.exchange(
                getPatientServiceUrl() + "/admissions/" + admissionId, HttpMethod.PUT, requestEntity, AdmissionsDTO.class);
        log.info("Successfully updated admission: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public void deleteAdmission(Long doctorId, Long patientId, Long admissionId) {
        log.info("Deleting admission ID {} for patient ID {} by doctor ID {}", admissionId, patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
        restTemplate.exchange(
                getPatientServiceUrl() + "/admissions/" + admissionId, HttpMethod.DELETE, requestEntity, Void.class);
        log.info("Successfully deleted admission ID {}", admissionId);
    }

    @Override
    public AppointmentsDTO createAppointmentForPatient(Long doctorId, Long patientId, AppointmentsDTO appointmentsDTO) {
        log.info("Creating appointment for patient ID {} by doctor ID {}", patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        appointmentsDTO.setId(null);
        appointmentsDTO.setPatientId(patientId);
        appointmentsDTO.setDoctorId(doctorId);
        HttpEntity<AppointmentsDTO> requestEntity = new HttpEntity<>(appointmentsDTO, getAuthHeaders());
        ResponseEntity<AppointmentsDTO> response = restTemplate.postForEntity(
                getPatientServiceUrl() + "/appointments", requestEntity, AppointmentsDTO.class);
        log.info("Successfully created appointment: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public AppointmentsDTO getAppointmentById(Long doctorId, Long patientId, Long appointmentId) {
        log.info("Fetching appointment ID {} for patient ID {} by doctor ID {}", appointmentId, patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<AppointmentsDTO> response = restTemplate.exchange(
                getPatientServiceUrl() + "/appointments/" + appointmentId, HttpMethod.GET, requestEntity, AppointmentsDTO.class);
        return response.getBody();
    }

    @Override
    public List<AppointmentsDTO> getAppointmentsByPatientId(Long doctorId, Long patientId) {
        log.info("Fetching appointments for patient ID {} by doctor ID {}", patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<AppointmentsDTO[]> response = restTemplate.exchange(
                getPatientServiceUrl() + "/appointments/patient/" + patientId, HttpMethod.GET, requestEntity, AppointmentsDTO[].class);
        return Arrays.asList(response.getBody());
    }

    @Override
    public AppointmentsDTO updateAppointment(Long doctorId, Long patientId, Long appointmentId, AppointmentsDTO appointmentsDTO) {
        log.info("Updating appointment ID {} for patient ID {} by doctor ID {}", appointmentId, patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        appointmentsDTO.setPatientId(patientId);
        appointmentsDTO.setDoctorId(doctorId);
        HttpEntity<AppointmentsDTO> requestEntity = new HttpEntity<>(appointmentsDTO, getAuthHeaders());
        ResponseEntity<AppointmentsDTO> response = restTemplate.exchange(
                getPatientServiceUrl() + "/appointments/" + appointmentId, HttpMethod.PUT, requestEntity, AppointmentsDTO.class);
        log.info("Successfully updated appointment: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public void deleteAppointment(Long doctorId, Long patientId, Long appointmentId) {
        log.info("Deleting appointment ID {} for patient ID {} by doctor ID {}", appointmentId, patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders());
        restTemplate.exchange(
                getPatientServiceUrl() + "/appointments/" + appointmentId, HttpMethod.DELETE, requestEntity, Void.class);
        log.info("Successfully deleted appointment ID {}", appointmentId);
    }

    @Override
    public PatientDataDTO getPatientData(Long doctorId, Long patientId) {
        log.info("Fetching patient data for patient ID {} by doctor ID {}", patientId, doctorId);
        validateDoctorPatientRelationship(doctorId, patientId);

        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (!patientOpt.isPresent()) {
            log.error("Patient not found for ID: {}", patientId);
            throw new RuntimeException("Patient not found");
        }
        Patient patient = patientOpt.get();
        PatientDTO patientDTO = toPatientDto(patient);

        List<MedicalRecordDTO> medicalRecords = getMedicalRecordsByPatientId(doctorId, patientId);
        List<AdmissionsDTO> admissions = getAdmissionsByPatientId(doctorId, patientId);
        List<AppointmentsDTO> appointments = getAppointmentsByPatientId(doctorId, patientId);

        PatientDataDTO patientData = new PatientDataDTO(
                patientId,
                patientDTO.getName(),
                medicalRecords,
                admissions,
                appointments
        );
        log.info("Successfully fetched patient data for patient ID {}: {}", patientId, patientData);
        return patientData;
    }

    private Doctor toEntity(DoctorDTO dto) {
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setUsername(dto.getUsername());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setDepartment(dto.getDepartment());
        doctor.setContactPhone(dto.getContactPhone());
        doctor.setContactEmail(dto.getContactEmail());
        doctor.setOfficeLocation(dto.getOfficeLocation());
        doctor.setSchedule(dto.getSchedule());
        if (dto.getPatientIds() != null && !dto.getPatientIds().isEmpty()) {
            List<Patient> patients = patientRepository.findAllById(dto.getPatientIds());
            if (patients.size() != dto.getPatientIds().size()) {
                log.error("One or more patient IDs not found: {}", dto.getPatientIds());
                throw new RuntimeException("One or more patient IDs not found");
            }
            doctor.setPatients(patients);
        }
        return doctor;
    }

    private DoctorDTO toDto(Doctor doctor) {
        return toDto(doctor, false); // Default to not include patients
    }

    private DoctorDTO toDto(Doctor doctor, boolean includePatients) {
        List<Long> patientIds = doctor.getPatients().stream()
                .map(Patient::getPatientId)
                .collect(Collectors.toList());
        List<PatientDTO> patients = includePatients ?
                doctor.getPatients().stream()
                        .map(this::toPatientDto)
                        .collect(Collectors.toList()) : new ArrayList<>();
        return new DoctorDTO(
                doctor.getDoctorId(),
                doctor.getName(),
                doctor.getUsername(),
                doctor.getSpecialization(),
                doctor.getDepartment(),
                doctor.getContactPhone(),
                doctor.getContactEmail(),
                doctor.getOfficeLocation(),
                doctor.getSchedule(),
                patientIds,
                patients
        );
    }

    private PatientDTO toPatientDto(Patient patient) {
        List<Long> doctorIds = patient.getDoctors().stream()
                .map(Doctor::getDoctorId)
                .collect(Collectors.toList());
        List<Long> nurseIds = patient.getNurses().stream()
                .map(Nurse::getNurseId)
                .collect(Collectors.toList());
        return new PatientDTO(
                patient.getPatientId(),
                patient.getName(),
                patient.getGender(),
                patient.getEmail(),
                patient.getPhone_number(),
                patient.getDob(),
                doctorIds,
                nurseIds
        );
    }
}