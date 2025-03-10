package com.bruceycode.Patient_Service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Data
public class MedicalRecordDTO {
    private Long Id;
    private Long patientId;
    private Long doctorId;
    private String condition;
    private LocalDate diagnosisDate;
    private String notes;

    public MedicalRecordDTO() {
    }

    public MedicalRecordDTO(Long id, Long patientId, Long doctorId, String condition, String notes, LocalDate diagnosisDate) {
        Id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.condition = condition;
        this.notes = notes;
        this.diagnosisDate = diagnosisDate;
    }
}
