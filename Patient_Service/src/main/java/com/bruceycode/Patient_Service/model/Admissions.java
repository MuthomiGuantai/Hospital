package com.bruceycode.Patient_Service.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
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

}