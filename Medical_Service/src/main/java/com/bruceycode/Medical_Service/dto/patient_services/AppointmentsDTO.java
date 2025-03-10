package com.bruceycode.Medical_Service.dto.patient_services;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Setter
@Getter
public class AppointmentsDTO {
    private Long Id;

    @Setter
    @Getter
    private Long patientId;
    private Long doctorId;
    private Long nurseId;
    private LocalDate appointmentDate;
    private String reason;

    public AppointmentsDTO() {
    }

    public AppointmentsDTO(Long id, Long patientId, Long doctorId, Long nurseId, LocalDate appointmentDate, String reason) {
        Id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.nurseId = nurseId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
    }
}
