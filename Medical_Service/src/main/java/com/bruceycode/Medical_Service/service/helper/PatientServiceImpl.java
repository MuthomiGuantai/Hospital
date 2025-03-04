package com.bruceycode.Medical_Service.service.helper;

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

@Slf4j
@RequiredArgsConstructor
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;

    @Override
    public Patient createPatient(Patient patient) {
        log.info("Creating patient: {}", patient);
        Patient savedPatient = patientRepository.save(patient);
        log.info("Successfully created patient: {}", savedPatient);
        return savedPatient;
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {
        log.info("Fetching patient by ID: {}", id);
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            log.info("Successfully fetched patient ID {}: {}", id, patient.get());
        } else {
            log.warn("No patient found for ID: {}", id);
        }
        return patient;
    }

    @Override
    public List<Patient> getAllPatients() {
        log.info("Fetching all patients");
        List<Patient> patients = patientRepository.findAll();
        log.info("Successfully fetched {} patients", patients.size());
        return patients;
    }

    @Override
    public Patient updatePatient(Long id, Patient patientDetails) {
        log.info("Updating patient ID {} with details: {}", id, patientDetails);
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patient.setName(patientDetails.getName());
            patient.setEmail(patientDetails.getEmail());
            patient.setPhone_number(patientDetails.getPhone_number());
            patient.setGender(patientDetails.getGender());
            patient.setDob(patientDetails.getDob());
            // Doctors and Nurses relationships are managed separately
            Patient updatedPatient = patientRepository.save(patient);
            log.info("Successfully updated patient ID {}: {}", id, updatedPatient);
            return updatedPatient;
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
    public Patient addDoctorToPatient(Long patientId, Long doctorId) {
        log.info("Adding doctor ID {} to patient ID: {}", doctorId, patientId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);

        if (patientOpt.isPresent() && doctorOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Doctor doctor = doctorOpt.get();
            patient.addDoctor(doctor);
            Patient updatedPatient = patientRepository.save(patient);
            log.info("Successfully added doctor ID {} to patient ID {}: {}", doctorId, patientId, updatedPatient);
            return updatedPatient;
        }
        log.error("Patient ID {} or Doctor ID {} not found", patientId, doctorId);
        throw new RuntimeException("Patient or Doctor not found");
    }

    @Override
    public Patient addNurseToPatient(Long patientId, Long nurseId) {
        log.info("Adding nurse ID {} to patient ID: {}", nurseId, patientId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Nurse> nurseOpt = nurseRepository.findById(nurseId);

        if (patientOpt.isPresent() && nurseOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Nurse nurse = nurseOpt.get();
            patient.addNurse(nurse);
            Patient updatedPatient = patientRepository.save(patient);
            log.info("Successfully added nurse ID {} to patient ID {}: {}", nurseId, patientId, updatedPatient);
            return updatedPatient;
        }
        log.error("Patient ID {} or Nurse ID {} not found", patientId, nurseId);
        throw new RuntimeException("Patient or Nurse not found");
    }
}