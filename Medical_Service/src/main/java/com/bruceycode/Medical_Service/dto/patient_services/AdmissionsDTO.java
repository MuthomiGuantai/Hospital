package com.bruceycode.Medical_Service.dto.patient_services;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public class AdmissionsDTO {
    private Long Id;
    private Long patientId;
    private Long doctorId;
    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private String reason;

    public AdmissionsDTO() {
    }

    public AdmissionsDTO(Long id, Long patientId, Long doctorId, LocalDate admissionDate, LocalDate dischargeDate, String reason) {
        Id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.reason = reason;
    }
}
