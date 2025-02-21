package com.bruceycode.Medical_Service.service.helper;

import com.bruceycode.Medical_Service.model.Doctor;
import com.bruceycode.Medical_Service.model.Patient;
import com.bruceycode.Medical_Service.repository.DoctorRepository;
import com.bruceycode.Medical_Service.repository.PatientRepository;
import com.bruceycode.Medical_Service.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
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
            return doctorRepository.save(doctor);
        }
        throw new RuntimeException("Doctor not found with id " + id);
    }

    @Override
    public void deleteDoctor(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            doctorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Doctor not found with id " + id);
        }
    }

    public Doctor addPatientToDoctor(Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        doctor.addPatient(patient);
        return doctorRepository.save(doctor);
    }
}
