package com.bruceycode.Medical_Service.dto.medical_services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class NurseDTO {

    private Long nurseId;
    private String name;
    private String username;
    private String department;
    private String contactPhone;
    private String contactEmail;
    private String shiftSchedule;
    private List<Long> patientIds = new ArrayList<>();
    @JsonDeserialize(contentUsing = PatientIdDeserializer.class)
    private List<PatientDTO> patients = new ArrayList<>();


    public NurseDTO() {
    }

    public NurseDTO(Long nurseId, String name, String username, String department, String contactEmail, String contactPhone, String shiftSchedule, List<Long> patientIds, List<PatientDTO> patients) {
        this.nurseId = nurseId;
        this.name = name;
        this.username = username;
        this.department = department;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.shiftSchedule = shiftSchedule;
        this.patientIds = patientIds != null ? patientIds : new ArrayList<>();
        this.patients = patients != null ? patients : new ArrayList<>();
    }
}
