package com.bruceycode.Patient_Service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class AppointmentsDTO {
    private Long Id;
    private Long doctorId;
    private Long nurseId;
    private LocalDate appointmentDate;
    private String reason;

    public AppointmentsDTO() {
    }

    public AppointmentsDTO(Long id, Long doctorId, Long nurseId, LocalDate appointmentDate, String reason) {
        Id = id;
        this.doctorId = doctorId;
        this.nurseId = nurseId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
    }
}
