package com.bruceycode.Patient_Service.model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "admissions")
public class Admissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    private LocalDate admissionDate;
    private LocalDate dischargeDate;
    private String reason;

    public Admissions() {}
    public Admissions(Long patientId, LocalDate admissionDate, LocalDate dischargeDate, String reason) {
        this.patientId = patientId;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.reason = reason;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }
    public LocalDate getDischargeDate() { return dischargeDate; }
    public void setDischargeDate(LocalDate dischargeDate) { this.dischargeDate = dischargeDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}