package com.bruceycode.Medical_Service.service.helper;

import com.bruceycode.Medical_Service.dto.medical_services.DoctorDTO;
import com.bruceycode.Medical_Service.dto.medical_services.PatientDTO;
import com.bruceycode.Medical_Service.model.entity.Doctor;
import com.bruceycode.Medical_Service.model.entity.Nurse;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.repository.DoctorRepository;
import com.bruceycode.Medical_Service.repository.PatientRepository;
import com.bruceycode.Medical_Service.service.DoctorService;
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
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

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