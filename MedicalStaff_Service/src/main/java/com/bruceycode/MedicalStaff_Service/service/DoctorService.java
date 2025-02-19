package com.bruceycode.MedicalStaff_Service.service;

import com.bruceycode.MedicalStaff_Service.model.Doctor;
import com.bruceycode.MedicalStaff_Service.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public Doctor registerDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        return doctorRepository.findById(id)
                .map(doctor -> {
                    doctor.setName(updatedDoctor.getName());
                    doctor.setSpecialization(updatedDoctor.getSpecialization());
                    doctor.setDepartment(updatedDoctor.getDepartment());
                    doctor.setPhone_number(updatedDoctor.getPhone_number());
                    doctor.setEmail(updatedDoctor.getEmail());
                    doctor.setPatients(updatedDoctor.getPatients());
                    return doctorRepository.save(doctor);
                })
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

}


