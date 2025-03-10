package com.bruceycode.Patient_Service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Setter
@Getter
public class AppointmentsDTO {
    private Long Id;
    private Long patientId;
    private Long doctorId;
    private Long nurseId;
    private LocalDateTime appointmentDate;
    private String reason;

    public AppointmentsDTO() {
    }

    public AppointmentsDTO(Long id, Long patientId, Long doctorId, Long nurseId, LocalDateTime appointmentDate, String reason) {
        Id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.nurseId = nurseId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
    }
}
