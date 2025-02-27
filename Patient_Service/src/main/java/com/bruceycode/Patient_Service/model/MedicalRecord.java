package com.bruceycode.Patient_Service.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "medical_records")
@Data
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public LocalDate getDiagnosisDate() { return diagnosisDate; }
    public void setDiagnosisDate(LocalDate diagnosisDate) { this.diagnosisDate = diagnosisDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}