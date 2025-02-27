package com.bruceycode.Medical_Service.service.helper;

import com.bruceycode.Medical_Service.model.entity.Nurse;
import com.bruceycode.Medical_Service.model.entity.Patient;
import com.bruceycode.Medical_Service.repository.NurseRepository;
import com.bruceycode.Medical_Service.repository.PatientRepository;
import com.bruceycode.Medical_Service.service.NurseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NurseServiceImpl implements NurseService {

    private static final Logger logger = LoggerFactory.getLogger(NurseServiceImpl.class);

    private final NurseRepository nurseRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    public NurseServiceImpl(NurseRepository nurseRepository) {
        this.nurseRepository = nurseRepository;
        logger.info("NurseServiceImpl initialized with NurseRepository");
    }

    @Override
    public Nurse createNurse(Nurse nurse) {
        logger.info("Creating nurse: {}", nurse);
        Nurse savedNurse = nurseRepository.save(nurse);
        logger.info("Successfully created nurse: {}", savedNurse);
        return savedNurse;
    }

    @Override
    public Optional<Nurse> getNurseById(Long id) {
        logger.info("Fetching nurse by ID: {}", id);
        Optional<Nurse> nurse = nurseRepository.findById(id);
        if (nurse.isPresent()) {
            logger.info("Successfully fetched nurse ID {}: {}", id, nurse.get());
        } else {
            logger.warn("No nurse found for ID: {}", id);
        }
        return nurse;
    }

    @Override
    public List<Nurse> getAllNurses() {
        logger.info("Fetching all nurses");
        List<Nurse> nurses = nurseRepository.findAll();
        logger.info("Successfully fetched {} nurses", nurses.size());
        return nurses;
    }

    @Override
    public Nurse updateNurse(Long id, Nurse nurseDetails) {
        logger.info("Updating nurse ID {} with details: {}", id, nurseDetails);
        Optional<Nurse> optionalNurse = nurseRepository.findById(id);

        if (optionalNurse.isPresent()) {
            Nurse nurse = optionalNurse.get();
            nurse.setName(nurseDetails.getName());
            nurse.setDepartment(nurseDetails.getDepartment());
            nurse.setContactPhone(nurseDetails.getContactPhone());
            nurse.setContactEmail(nurseDetails.getContactEmail());
            nurse.setShiftSchedule(nurseDetails.getShiftSchedule());
            // Note: Patients relationship needs special handling if updating
            Nurse updatedNurse = nurseRepository.save(nurse);
            logger.info("Successfully updated nurse ID {}: {}", id, updatedNurse);
            return updatedNurse;
        }
        logger.error("Nurse not found for update with ID: {}", id);
        throw new RuntimeException("Nurse not found with id " + id);
    }

    @Override
    public void deleteNurse(Long id) {
        logger.info("Deleting nurse with ID: {}", id);
        Optional<Nurse> nurse = nurseRepository.findById(id);
        if (nurse.isPresent()) {
            nurseRepository.deleteById(id);
            logger.info("Successfully deleted nurse with ID: {}", id);
        } else {
            logger.error("Nurse not found for deletion with ID: {}", id);
            throw new RuntimeException("Nurse not found with id " + id);
        }
    }

    @Override
    public Nurse addPatientToNurse(Long nurseId, Long patientId) {
        logger.info("Adding patient ID {} to nurse ID: {}", patientId, nurseId);
        Nurse nurse = nurseRepository.findById(nurseId)
                .orElseThrow(() -> {
                    logger.error("Nurse not found with ID: {}", nurseId);
                    return new RuntimeException("Nurse not found");
                });
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> {
                    logger.error("Patient not found with ID: {}", patientId);
                    return new RuntimeException("Patient not found");
                });

        nurse.addPatient(patient);
        Nurse updatedNurse = nurseRepository.save(nurse);
        logger.info("Successfully added patient ID {} to nurse ID {}: {}", patientId, nurseId, updatedNurse);
        return updatedNurse;
    }
}