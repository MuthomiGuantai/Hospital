package com.bruceycode.Medical_Service.service.helper;

import com.bruceycode.Medical_Service.model.Doctor;
import com.bruceycode.Medical_Service.model.Nurse;
import com.bruceycode.Medical_Service.model.Patient;
import com.bruceycode.Medical_Service.repository.DoctorRepository;
import com.bruceycode.Medical_Service.repository.NurseRepository;
import com.bruceycode.Medical_Service.repository.PatientRepository;
import com.bruceycode.Medical_Service.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

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
    }

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient updatePatient(Long id, Patient patientDetails) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patient.setName(patientDetails.getName());
            patient.setEmail(patientDetails.getEmail());
            patient.setPhone_number(patientDetails.getPhone_number());
            patient.setGender(patientDetails.getGender());
            patient.setDob(patientDetails.getDob());
            // Note: Doctors and Nurses relationships are managed separately
            return patientRepository.save(patient);
        }
        throw new RuntimeException("Patient not found with id " + id);
    }

    @Override
    public void deletePatient(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            patientRepository.deleteById(id);
        } else {
            throw new RuntimeException("Patient not found with id " + id);
        }
    }

    @Override
    public Patient addDoctorToPatient(Long patientId, Long doctorId) {
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);

        if (patientOpt.isPresent() && doctorOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Doctor doctor = doctorOpt.get();
            patient.addDoctor(doctor);
            return patientRepository.save(patient);
        }
        throw new RuntimeException("Patient or Doctor not found");
    }

    @Override
    public Patient addNurseToPatient(Long patientId, Long nurseId) {
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        Optional<Nurse> nurseOpt = nurseRepository.findById(nurseId);

        if (patientOpt.isPresent() && nurseOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Nurse nurse = nurseOpt.get();
            patient.addNurse(nurse);
            return patientRepository.save(patient);
        }
        throw new RuntimeException("Patient or Nurse not found");
    }
}