package com.bruceycode.Patient_Service.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class PatientDataDTO {
    private Long patientId;
    private String name;
    private List<MedicalRecordDTO> medicalRecords;
    private List<AdmissionsDTO> admissions;
    private List<AppointmentsDTO> appointments;

    public PatientDataDTO() {
    }

    public PatientDataDTO(Long patientId, String name, List<MedicalRecordDTO> medicalRecords, List<AdmissionsDTO> admissions, List<AppointmentsDTO> appointments) {
        this.patientId = patientId;
        this.name = name;
        this.medicalRecords = medicalRecords;
        this.admissions = admissions;
        this.appointments = appointments;
    }
}
