package com.bruceycode.Medical_Service.dto.patient_services;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@Data
public class MedicalRecordDTO {
    private Long Id;
    private String condition;
    private LocalDate diagnosisDate;
    private String notes;

    public MedicalRecordDTO() {
    }

    public MedicalRecordDTO(Long id, String condition, String notes, LocalDate diagnosisDate) {
        Id = id;
        this.condition = condition;
        this.notes = notes;
        this.diagnosisDate = diagnosisDate;
    }
}
