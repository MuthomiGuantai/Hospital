package com.bruceycode.Patient_Service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "appointments")
public class Appointments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Setter
    @Getter
    @Column(name = "nurse_id", nullable = false)
    private Long nurseId;

    private LocalDateTime appointmentDate;
    private String reason;

    public Appointments() {}
    public Appointments(Long patientId, Long doctorId, Long nurseId, LocalDateTime appointmentDate, String reason) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.nurseId = nurseId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
    }

}