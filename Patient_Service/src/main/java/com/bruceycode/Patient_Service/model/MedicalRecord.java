package com.bruceycode.Patient_Service.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "medical_records")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "`condition`")
    private String condition;
    private LocalDate diagnosisDate;
    private String notes;

    public MedicalRecord() {}
    public MedicalRecord(Long patientId, Long doctorId, String condition, LocalDate diagnosisDate, String notes) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.condition = condition;
        this.diagnosisDate = diagnosisDate;
        this.notes = notes;
    }

}