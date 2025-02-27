package com.bruceycode.Medical_Service.service.helper;

import com.bruceycode.Medical_Service.model.entity.Doctor;
import com.bruceycode.Medical_Service.model.entity.Nurse;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.repository.DoctorRepository;
import com.bruceycode.Medical_Service.repository.NurseRepository;
import com.bruceycode.Medical_Service.repository.PatientRepository;
import com.bruceycode.Medical_Service.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientServiceImpl.class);

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              NurseRepository nurseRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.nurseRepository = nurseRepository;
        logger.info("PatientServiceImpl initialized with repositories");
    }

    @Override
    public Patient createPatient(Patient patient) {
        logger.info("Creating patient: {}", patient);
        Patient savedPatient = patientRepository.save(patient);
        logger.info("Successfully created patient: {}", savedPatient);
        return savedPatient;
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {
        logger.info("Fetching patient by ID: {}", id);
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            logger.info("Successfully fetched patient ID {}: {}", id, patient.get());
        } else {
            logger.warn("No patient found for ID: {}", id);
        }
        return patient;
    }

    @Override
    public List<Patient> getAllPatients() {
        logger.info("Fetching all patients");
        List<Patient> patients = patientRepository.findAll();
        logger.info("Successfully fetched {} patients", patients.size());
        return patients;
    }

    @Override
    public Patient updatePatient(Long id, Patient patientDetails) {
        logger.info("Updating patient ID {} with details: {}", id, patientDetails);
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patient.setName(patientDetails.getName());
            patient.setEmail(patientDetails.getEmail());
            patient.setPhone_number(patientDetails.getPhone_number());
            patient.setGender(patientDetails.getGender());
            patient.setDob(patientDetails.getDob());
            // Note: Doctors and Nurses relationships are managed separately
            Patient updatedPatient = patientRepository.save(patient);
            logger.info("Successfully updated patient ID {}: {}", id, updatedPatient);
            return updatedPatient;
        }
        logger.error("Patient not found for update with ID: {}", id);
        throw new RuntimeException("Patient not found with id " + id);
    }

    @Override
    public void deletePatient(Long id) {
        logger.info("Deleting patient with ID: {}", id);
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            patientRepository.deleteById(id);
            logger.info("Successfully deleted patient with ID: {}", id);
        } else {
            logger.error("Patient not found for deletion with ID: {}", id);
            throw new RuntimeException("Patient not found with id " + id);
        }
    }

    @Override
    public Patient addDoctorToPatient(Long patientId, Long doctorId) {
        logger.info("Adding doctor ID {} to patient ID: {}", doctorId, patientId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);

        if (patientOpt.isPresent() && doctorOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Doctor doctor = doctorOpt.get();
            patient.addDoctor(doctor);
            Patient updatedPatient = patientRepository.save(patient);
            logger.info("Successfully added doctor ID {} to patient ID {}: {}", doctorId, patientId, updatedPatient);
            return updatedPatient;
        }
        logger.error("Patient ID {} or Doctor ID {} not found", patientId, doctorId);
        throw new RuntimeException("Patient or Doctor not found");
    }

    @Override
    public Patient addNurseToPatient(Long patientId, Long nurseId) {
        logger.info("Adding nurse ID {} to patient ID: {}", nurseId, patientId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Nurse> nurseOpt = nurseRepository.findById(nurseId);

        if (patientOpt.isPresent() && nurseOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Nurse nurse = nurseOpt.get();
            patient.addNurse(nurse);
            Patient updatedPatient = patientRepository.save(patient);
            logger.info("Successfully added nurse ID {} to patient ID {}: {}", nurseId, patientId, updatedPatient);
            return updatedPatient;
        }
        logger.error("Patient ID {} or Nurse ID {} not found", patientId, nurseId);
        throw new RuntimeException("Patient or Nurse not found");
    }
}