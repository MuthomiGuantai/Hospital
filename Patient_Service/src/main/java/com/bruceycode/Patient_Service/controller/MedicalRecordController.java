package com.bruceycode.Patient_Service.controller;

import com.bruceycode.Patient_Service.model.MedicalRecord;
import com.bruceycode.Patient_Service.service.MedicalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
        logger.info("MedicalRecordController initialized with MedicalRecordService");
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("Received POST request to create medical record: {}", medicalRecord);
        MedicalRecord createdRecord = medicalRecordService.createMedicalRecord(medicalRecord);
        logger.info("Successfully created medical record: {}", createdRecord);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        logger.info("Received GET request for medical record ID: {}", id);
        Optional<MedicalRecord> record = medicalRecordService.getMedicalRecordById(id);
        if (record.isPresent()) {
            logger.info("Successfully retrieved medical record ID {}: {}", id, record.get());
            return new ResponseEntity<>(record.get(), HttpStatus.OK);
        } else {
            logger.warn("Medical record not found for ID: {}", id);
            throw new RuntimeException("Medical record not found for ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        logger.info("Received GET request for all medical records");
        List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();
        logger.info("Successfully retrieved {} medical records", records.size());
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByPatientId(@PathVariable Long patientId) {
        logger.info("Received GET request for medical records of patient ID: {}", patientId);
        List<MedicalRecord> records = medicalRecordService.getMedicalRecordsByPatientId(patientId);
        logger.info("Successfully retrieved {} medical records for patient ID: {}", records.size(), patientId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord) {
        logger.info("Received PUT request to update medical record ID {} with details: {}", id, medicalRecord);
        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(id, medicalRecord);
        logger.info("Successfully updated medical record ID {}: {}", id, updatedRecord);
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        logger.info("Received DELETE request for medical record ID: {}", id);
        medicalRecordService.deleteMedicalRecord(id);
        logger.info("Successfully deleted medical record ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}