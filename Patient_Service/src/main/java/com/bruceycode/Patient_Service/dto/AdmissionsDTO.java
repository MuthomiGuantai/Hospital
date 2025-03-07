package com.bruceycode.Patient_Service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AdmissionsDTO {
    private Long Id;
    private Long patientId;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private String reason;

    public AdmissionsDTO() {
    }

    public AdmissionsDTO(Long id, Long patientId, LocalDate admissionDate, LocalDate dischargeDate, String reason) {
        Id = id;
        this.patientId = patientId;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.reason = reason;
    }
}
