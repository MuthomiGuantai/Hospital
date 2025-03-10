package com.bruceycode.Patient_Service.controller;

import com.bruceycode.Patient_Service.dto.MedicalRecordDTO;
import com.bruceycode.Patient_Service.model.MedicalRecord;
import com.bruceycode.Patient_Service.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@RequestBody MedicalRecordDTO medicalRecordDTO) {
        log.info("Received POST request to create medical record: {}", medicalRecordDTO);
        MedicalRecord medicalRecord = toEntity(medicalRecordDTO);
        MedicalRecord createdRecord = medicalRecordService.createMedicalRecord(medicalRecord);
        log.info("Successfully created medical record: {}", createdRecord);
        return new ResponseEntity<>(toDto(createdRecord), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordById(@PathVariable Long id) {
        log.info("Received GET request for medical record ID: {}", id);
        Optional<MedicalRecord> record = medicalRecordService.getMedicalRecordById(id);
        if (record.isPresent()) {
            log.info("Successfully retrieved medical record ID {}: {}", id, record.get());
            return new ResponseEntity<>(toDto(record.get()), HttpStatus.OK);
        } else {
            log.warn("Medical record not found for ID: {}", id);
            throw new RuntimeException("Medical record not found for ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecordDTO>> getAllMedicalRecords() {
        log.info("Received GET request for all medical records");
        List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();
        log.info("Successfully retrieved {} medical records", records.size());
        return new ResponseEntity<>(records.stream().map(this::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByPatientId(@PathVariable Long patientId) {
        log.info("Received GET request for medical records of patient ID: {}", patientId);
        List<MedicalRecord> records = medicalRecordService.getMedicalRecordsByPatientId(patientId);
        log.info("Successfully retrieved {} medical records for patient ID: {}", records.size(), patientId);
        return new ResponseEntity<>(records.stream().map(this::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecordDTO medicalRecordDTO) {
        log.info("Received PUT request to update medical record ID {} with details: {}", id, medicalRecordDTO);
        MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(id, toEntity(medicalRecordDTO));
        log.info("Successfully updated medical record ID {}: {}", id, updatedRecord);
        return new ResponseEntity<>(toDto(updatedRecord), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        log.info("Received DELETE request for medical record ID: {}", id);
        medicalRecordService.deleteMedicalRecord(id);
        log.info("Successfully deleted medical record ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private MedicalRecord toEntity(MedicalRecordDTO dto) {
        MedicalRecord entity = new MedicalRecord();
        entity.setId(dto.getId());
        entity.setPatientId(dto.getPatientId());
        entity.setDoctorId(dto.getDoctorId());
        entity.setCondition(dto.getCondition());
        entity.setDiagnosisDate(dto.getDiagnosisDate());
        entity.setNotes(dto.getNotes());
        return entity;
    }

    private MedicalRecordDTO toDto(MedicalRecord entity) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setId(entity.getId());
        dto.setPatientId(entity.getPatientId());
        dto.setDoctorId(entity.getDoctorId());
        dto.setCondition(entity.getCondition());
        dto.setDiagnosisDate(entity.getDiagnosisDate());
        dto.setNotes(entity.getNotes());
        return dto;
    }
}