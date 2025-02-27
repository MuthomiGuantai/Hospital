package com.bruceycode.Medical_Service.service.helper;

import com.bruceycode.Medical_Service.model.entity.Doctor;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.repository.DoctorRepository;
import com.bruceycode.Medical_Service.repository.PatientRepository;
import com.bruceycode.Medical_Service.service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
        logger.info("DoctorServiceImpl initialized with DoctorRepository");
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        logger.info("Creating doctor: {}", doctor);
        Doctor savedDoctor = doctorRepository.save(doctor);
        logger.info("Successfully created doctor: {}", savedDoctor);
        return savedDoctor;
    }

    @Override
    public Optional<Doctor> getDoctorById(Long id) {
        logger.info("Fetching doctor by ID: {}", id);
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            logger.info("Successfully fetched doctor ID {}: {}", id, doctor.get());
        } else {
            logger.warn("No doctor found for ID: {}", id);
        }
        return doctor;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        logger.info("Fetching all doctors");
        List<Doctor> doctors = doctorRepository.findAll();
        logger.info("Successfully fetched {} doctors", doctors.size());
        return doctors;
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        logger.info("Updating doctor ID {} with details: {}", id, doctorDetails);
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);

        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            doctor.setName(doctorDetails.getName());
            doctor.setSpecialization(doctorDetails.getSpecialization());
            doctor.setDepartment(doctorDetails.getDepartment());
            doctor.setContactPhone(doctorDetails.getContactPhone());
            doctor.setContactEmail(doctorDetails.getContactEmail());
            doctor.setOfficeLocation(doctorDetails.getOfficeLocation());
            doctor.setSchedule(doctorDetails.getSchedule());
            // Note: Patients relationship needs special handling if you're updating it
            Doctor updatedDoctor = doctorRepository.save(doctor);
            logger.info("Successfully updated doctor ID {}: {}", id, updatedDoctor);
            return updatedDoctor;
        }
        logger.error("Doctor not found for update with ID: {}", id);
        throw new RuntimeException("Doctor not found with id " + id);
    }

    @Override
    public void deleteDoctor(Long id) {
        logger.info("Deleting doctor with ID: {}", id);
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            doctorRepository.deleteById(id);
            logger.info("Successfully deleted doctor with ID: {}", id);
        } else {
            logger.error("Doctor not found for deletion with ID: {}", id);
            throw new RuntimeException("Doctor not found with id " + id);
        }
    }

    @Override
    public Doctor addPatientToDoctor(Long doctorId, Long patientId) {
        logger.info("Adding patient ID {} to doctor ID: {}", patientId, doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> {
                    logger.error("Doctor not found with ID: {}", doctorId);
                    return new RuntimeException("Doctor not found");
                });
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> {
                    logger.error("Patient not found with ID: {}", patientId);
                    return new RuntimeException("Patient not found");
                });

        doctor.addPatient(patient);
        Doctor updatedDoctor = doctorRepository.save(doctor);
        logger.info("Successfully added patient ID {} to doctor ID {}: {}", patientId, doctorId, updatedDoctor);
        return updatedDoctor;
    }
}