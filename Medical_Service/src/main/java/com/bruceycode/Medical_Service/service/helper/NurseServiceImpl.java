package com.bruceycode.Medical_Service.service.helper;

import com.bruceycode.Medical_Service.model.entity.Nurse;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.repository.NurseRepository;
import com.bruceycode.Medical_Service.repository.PatientRepository;
import com.bruceycode.Medical_Service.service.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NurseServiceImpl implements NurseService {

    private final NurseRepository nurseRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    public NurseServiceImpl(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
    }

    @Override
    public Nurse createNurse(Nurse nurse) {
        return nurseRepository.save(nurse);
    }

    @Override
    public Optional<Nurse> getNurseById(Long id) {
        return nurseRepository.findById(id);
    }

    @Override
    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    @Override
    public Nurse updateNurse(Long id, Nurse nurseDetails) {
        Optional<Nurse> optionalNurse = nurseRepository.findById(id);

        if (optionalNurse.isPresent()) {
            Nurse nurse = optionalNurse.get();
            nurse.setName(nurseDetails.getName());
            nurse.setDepartment(nurseDetails.getDepartment());
            nurse.setContactPhone(nurseDetails.getContactPhone());
            nurse.setContactEmail(nurseDetails.getContactEmail());
            nurse.setShiftSchedule(nurseDetails.getShiftSchedule());
            // Note: Patients relationship needs special handling if updating
            return nurseRepository.save(nurse);
        }
        throw new RuntimeException("Nurse not found with id " + id);
    }

    @Override
    public void deleteNurse(Long id) {
        Optional<Nurse> nurse = nurseRepository.findById(id);
        if (nurse.isPresent()) {
            nurseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Nurse not found with id " + id);
        }
    }
    public Nurse addPatientToNurse(Long nurseId, Long patientId) {
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> new RuntimeException("Nurse not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        nurse.addPatient(patient);
        return nurseRepository.save(nurse);
    }
}