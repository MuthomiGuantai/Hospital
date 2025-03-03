package com.bruceycode.Medical_Service.service.helper;

import com.bruceycode.Medical_Service.model.entity.Doctor;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.repository.DoctorRepository;
import com.bruceycode.Medical_Service.repository.PatientRepository;
import com.bruceycode.Medical_Service.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Doctor createDoctor(Doctor doctor) {
        log.info("Creating doctor: {}", doctor);
        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("Successfully created doctor: {}", savedDoctor);
        return savedDoctor;
    }

    @Override
    public Optional<Doctor> getDoctorById(Long id) {
        log.info("Fetching doctor by ID: {}", id);
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            log.info("Successfully fetched doctor ID {}: {}", id, doctor.get());
        } else {
            log.warn("No doctor found for ID: {}", id);
        }
        return doctor;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        log.info("Fetching all doctors");
        List<Doctor> doctors = doctorRepository.findAll();
        log.info("Successfully fetched {} doctors", doctors.size());
        return doctors;
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        log.info("Updating doctor ID {} with details: {}", id, doctorDetails);
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
            log.info("Successfully updated doctor ID {}: {}", id, updatedDoctor);
            return updatedDoctor;
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
    public Doctor addPatientToDoctor(Long doctorId, Long patientId) {
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
        return updatedDoctor;
    }
}