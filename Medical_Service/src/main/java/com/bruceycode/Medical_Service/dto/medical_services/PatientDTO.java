package com.bruceycode.Medical_Service.dto.medical_services;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PatientDTO {

    private Long patientId;
    private String name;
    private String gender;
    private String email;
    private Integer phone_number;
    private LocalDate dob;
    private List<Long> doctorIds = new ArrayList<>();
    private List<Long> nurseIds = new ArrayList<>();

    public PatientDTO() {
    }

    public PatientDTO(Long patientId, String name, String gender, String email, Integer phone_number, LocalDate dob, List<Long> doctorIds, List<Long> nurseIds) {
        this.patientId = patientId;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone_number = phone_number;
        this.dob = dob;
        this.doctorIds = doctorIds;
        this.nurseIds = nurseIds;
    }
}
