package com.bruceycode.Medical_Service.service.helper;

import com.bruceycode.Medical_Service.dto.medical_services.NurseDTO;
import com.bruceycode.Medical_Service.dto.medical_services.PatientDTO;
import com.bruceycode.Medical_Service.model.entity.Doctor;
import com.bruceycode.Medical_Service.model.entity.Nurse;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.repository.NurseRepository;
import com.bruceycode.Medical_Service.repository.PatientRepository;
import com.bruceycode.Medical_Service.service.NurseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public NurseDTO createNurse(NurseDTO nurseDTO) {
        log.info("Creating nurse: {}", nurseDTO);
        Nurse nurse = toEntity(nurseDTO);
        Nurse savedNurse = nurseRepository.save(nurse);
        log.info("Successfully created nurse: {}", savedNurse);
        return toDto(savedNurse);
    }

    @Override
    public Optional<NurseDTO> getNurseById(Long id, boolean includePatients) {
        log.info("Fetching nurse by ID: {}, includePatients: {}", id, includePatients);
        Optional<Nurse> nurse = nurseRepository.findById(id);
        if (nurse.isPresent()) {
            log.info("Successfully fetched nurse ID {}: {}", id, nurse.get());
            return Optional.of(toDto(nurse.get(), includePatients));
        } else {
            log.warn("No nurse found for ID: {}", id);
            return Optional.empty();
        }
    }

    @Override
    public List<NurseDTO> getAllNurses() {
        return getAllNurses(false); // Default to not include patients for backward compatibility
    }

    @Override
    public List<NurseDTO> getAllNurses(boolean includePatients) {
        log.info("Fetching all nurses, includePatients: {}", includePatients);
        List<Nurse> nurses = nurseRepository.findAll();
        log.info("Successfully fetched {} nurses", nurses.size());
        return nurses.stream()
                .map(nurse -> toDto(nurse, includePatients))
                .collect(Collectors.toList());
    }

    @Override
    public NurseDTO updateNurse(Long id, NurseDTO nurseDetails) {
        log.info("Updating nurse ID {} with details: {}", id, nurseDetails);
        Optional<Nurse> optionalNurse = nurseRepository.findById(id);

        if (optionalNurse.isPresent()) {
            Nurse nurse = optionalNurse.get();
            nurse.setName(nurseDetails.getName());
            nurse.setUsername(nurseDetails.getUsername());
            nurse.setDepartment(nurseDetails.getDepartment());
            nurse.setContactPhone(nurseDetails.getContactPhone());
            nurse.setContactEmail(nurseDetails.getContactEmail());
            nurse.setShiftSchedule(nurseDetails.getShiftSchedule());
            if (nurseDetails.getPatientIds() != null) {
                List<Patient> patients = patientRepository.findAllById(nurseDetails.getPatientIds());
                if (patients.size() != nurseDetails.getPatientIds().size()) {
                    log.error("One or more patient IDs not found for nurse ID: {}", id);
                    throw new RuntimeException("One or more patient IDs not found");
                }
                nurse.setPatients(patients);
            }
            Nurse updatedNurse = nurseRepository.save(nurse);
            log.info("Successfully updated nurse ID {}: {}", id, updatedNurse);
            return toDto(updatedNurse);
        }
        log.error("Nurse not found for update with ID: {}", id);
        throw new RuntimeException("Nurse not found with id " + id);
    }

    @Override
    public void deleteNurse(Long id) {
        log.info("Deleting nurse with ID: {}", id);
        Optional<Nurse> nurse = nurseRepository.findById(id);
        if (nurse.isPresent()) {
            nurseRepository.deleteById(id);
            log.info("Successfully deleted nurse with ID: {}", id);
        } else {
            log.error("Nurse not found for deletion with ID: {}", id);
            throw new RuntimeException("Nurse not found with id " + id);
        }
    }

    @Override
    public NurseDTO addPatientToNurse(Long nurseId, Long patientId) {
        log.info("Adding patient ID {} to nurse ID: {}", patientId, nurseId);
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> {
                    log.error("Nurse not found with ID: {}", nurseId);
                    return new RuntimeException("Nurse not found");
                });
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> {
                    log.error("Patient not found with ID: {}", patientId);
                    return new RuntimeException("Patient not found");
                });

        nurse.addPatient(patient);
        Nurse updatedNurse = nurseRepository.save(nurse);
        log.info("Successfully added patient ID {} to nurse ID {}: {}", patientId, nurseId, updatedNurse);
        return toDto(updatedNurse);
    }

    private Nurse toEntity(NurseDTO dto) {
        Nurse nurse = new Nurse();
        nurse.setName(dto.getName());
        nurse.setUsername(dto.getUsername());
        nurse.setDepartment(dto.getDepartment());
        nurse.setContactPhone(dto.getContactPhone());
        nurse.setContactEmail(dto.getContactEmail());
        nurse.setShiftSchedule(dto.getShiftSchedule());
        if (dto.getPatientIds() != null && !dto.getPatientIds().isEmpty()) {
            List<Patient> patients = patientRepository.findAllById(dto.getPatientIds());
            if (patients.size() != dto.getPatientIds().size()) {
                log.error("One or more patient IDs not found: {}", dto.getPatientIds());
                throw new RuntimeException("One or more patient IDs not found");
            }
            nurse.setPatients(patients);
        }
        return nurse;
    }

    private NurseDTO toDto(Nurse nurse) {
        return toDto(nurse, false); // Default to not include patients
    }

    private NurseDTO toDto(Nurse nurse, boolean includePatients) {
        List<Long> patientIds = nurse.getPatients().stream()
                .map(Patient::getPatientId)
                .collect(Collectors.toList());
        List<PatientDTO> patients = includePatients ?
                nurse.getPatients().stream()
                        .map(this::toPatientDto)
                        .collect(Collectors.toList()) : new ArrayList<>();
        return new NurseDTO(
                nurse.getNurseId(),
                nurse.getName(),
                nurse.getUsername(),
                nurse.getDepartment(),
                nurse.getContactEmail(),
                nurse.getContactPhone(),
                nurse.getShiftSchedule(),
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