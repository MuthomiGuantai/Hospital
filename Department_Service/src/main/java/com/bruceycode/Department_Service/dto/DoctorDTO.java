package com.bruceycode.Department_Service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialization;
    private String schedule;

    public DoctorDTO() {}

    public DoctorDTO(Long id, String name, String specialization, String schedule) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.schedule = schedule;
    }

}