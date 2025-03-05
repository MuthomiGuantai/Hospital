package com.bruceycode.Medical_Service.service.helper;

import com.bruceycode.Medical_Service.dto.medical_services.PatientDTO;
import com.bruceycode.Medical_Service.model.entity.Doctor;
import com.bruceycode.Medical_Service.model.entity.Nurse;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.repository.DoctorRepository;
import com.bruceycode.Medical_Service.repository.NurseRepository;
import com.bruceycode.Medical_Service.repository.PatientRepository;
import com.bruceycode.Medical_Service.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        log.info("Creating patient: {}", patientDTO);
        Patient patient = toEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patient);
        log.info("Successfully created patient: {}", savedPatient);
        return toDto(savedPatient);
    }

    @Override
    public Optional<PatientDTO> getPatientById(Long id) {
        log.info("Fetching patient by ID: {}", id);
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            log.info("Successfully fetched patient ID {}: {}", id, patient.get());
        } else {
            log.warn("No patient found for ID: {}", id);
        }
        return patient.map(this::toDto);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        log.info("Fetching all patients");
        List<Patient> patients = patientRepository.findAll();
        log.info("Successfully fetched {} patients", patients.size());
        return patients.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patientDetails) {
        log.info("Updating patient ID {} with details: {}", id, patientDetails);
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patient.setName(patientDetails.getName());
            patient.setEmail(patientDetails.getEmail());
            patient.setPhone_number(patientDetails.getPhone_number());
            patient.setGender(patientDetails.getGender());
            patient.setDob(patientDetails.getDob());
            // Update doctors and nurses if provided
            if (patientDetails.getDoctorIds() != null) {
                List<Doctor> doctors = doctorRepository.findAllById(patientDetails.getDoctorIds());
                if (doctors.size() != patientDetails.getDoctorIds().size()) {
                    log.error("One or more doctor IDs not found for patient ID: {}", id);
                    throw new RuntimeException("One or more doctor IDs not found");
                }
                patient.setDoctors(doctors);
            }
            if (patientDetails.getNurseIds() != null) {
                List<Nurse> nurses = nurseRepository.findAllById(patientDetails.getNurseIds());
                if (nurses.size() != patientDetails.getNurseIds().size()) {
                    log.error("One or more nurse IDs not found for patient ID: {}", id);
                    throw new RuntimeException("One or more nurse IDs not found");
                }
                patient.setNurses(nurses);
            }
            Patient updatedPatient = patientRepository.save(patient);
            log.info("Successfully updated patient ID {}: {}", id, updatedPatient);
            return toDto(updatedPatient);
        }
        log.error("Patient not found for update with ID: {}", id);
        throw new RuntimeException("Patient not found with id " + id);
    }

    @Override
    public void deletePatient(Long id) {
        log.info("Deleting patient with ID: {}", id);
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            patientRepository.deleteById(id);
            log.info("Successfully deleted patient with ID: {}", id);
        } else {
            log.error("Patient not found for deletion with ID: {}", id);
            throw new RuntimeException("Patient not found with id " + id);
        }
    }

    @Override
    public PatientDTO addDoctorToPatient(Long patientId, Long doctorId) {
        log.info("Adding doctor ID {} to patient ID: {}", doctorId, patientId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);

        if (patientOpt.isPresent() && doctorOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Doctor doctor = doctorOpt.get();
            patient.addDoctor(doctor);
            Patient updatedPatient = patientRepository.save(patient);
            log.info("Successfully added doctor ID {} to patient ID {}: {}", doctorId, patientId, updatedPatient);
            return toDto(updatedPatient);
        }
        log.error("Patient ID {} or Doctor ID {} not found", patientId, doctorId);
        throw new RuntimeException("Patient or Doctor not found");
    }

    @Override
    public PatientDTO addNurseToPatient(Long patientId, Long nurseId) {
        log.info("Adding nurse ID {} to patient ID: {}", nurseId, patientId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Nurse> nurseOpt = nurseRepository.findById(nurseId);

        if (patientOpt.isPresent() && nurseOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Nurse nurse = nurseOpt.get();
            patient.addNurse(nurse);
            Patient updatedPatient = patientRepository.save(patient);
            log.info("Successfully added nurse ID {} to patient ID {}: {}", nurseId, patientId, updatedPatient);
            return toDto(updatedPatient);
        }
        log.error("Patient ID {} or Nurse ID {} not found", patientId, nurseId);
        throw new RuntimeException("Patient or Nurse not found");
    }

    private Patient toEntity(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setGender(dto.getGender());
        patient.setEmail(dto.getEmail());
        patient.setPhone_number(dto.getPhone_number());
        patient.setDob(dto.getDob());
        if (dto.getDoctorIds() != null && !dto.getDoctorIds().isEmpty()) {
            List<Doctor> doctors = doctorRepository.findAllById(dto.getDoctorIds());
            if (doctors.size() != dto.getDoctorIds().size()) {
                log.error("One or more doctor IDs not found: {}", dto.getDoctorIds());
                throw new RuntimeException("One or more doctor IDs not found");
            }
            patient.setDoctors(doctors);
        }
        if (dto.getNurseIds() != null && !dto.getNurseIds().isEmpty()) {
            List<Nurse> nurses = nurseRepository.findAllById(dto.getNurseIds());
            if (nurses.size() != dto.getNurseIds().size()) {
                log.error("One or more nurse IDs not found: {}", dto.getNurseIds());
                throw new RuntimeException("One or more nurse IDs not found");
            }
            patient.setNurses(nurses);
        }
        return patient;
    }

    private PatientDTO toDto(Patient patient) {
        List<Long> doctorIds = patient.getDoctors().stream()
                .map(Doctor::getDoctorId)
                .collect(Collectors.toList());
        List<Long> nurseIds = patient.getNurses().stream()
                .map(Nurse::getNurseId)
                .collect(Collectors.toList());
        PatientDTO dto = new PatientDTO();
        dto.setPatientId(patient.getPatientId());
        dto.setName(patient.getName());
        dto.setGender(patient.getGender());
        dto.setEmail(patient.getEmail());
        dto.setPhone_number(patient.getPhone_number());
        dto.setDob(patient.getDob());
        dto.setDoctorIds(doctorIds);
        dto.setNurseIds(nurseIds);
        return dto;
    }
}