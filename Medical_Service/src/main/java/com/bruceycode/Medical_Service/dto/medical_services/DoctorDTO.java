package com.bruceycode.Medical_Service.dto.medical_services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class DoctorDTO {

    private Long doctorId;
    private String name;
    private String username;
    private String specialization;
    private String department;
    private String contactPhone;
    private String contactEmail;
    private String officeLocation;
    private String schedule;
    private List<Long> patientIds = new ArrayList<>();
    @JsonDeserialize(contentUsing = PatientIdDeserializer.class)
    private List<PatientDTO> patients = new ArrayList<>();

    public DoctorDTO() {
    }

    public DoctorDTO(Long doctorId, String name, String username, String specialization, String department, String contactPhone, String contactEmail, String officeLocation, String schedule, List<Long> patientIds, List<PatientDTO> patients) {
        this.doctorId = doctorId;
        this.name = name;
        this.username = username;
        this.specialization = specialization;
        this.department = department;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.officeLocation = officeLocation;
        this.schedule = schedule;
        this.patientIds = patientIds != null ? patientIds : new ArrayList<>();
        this.patients = patients != null ? patients : new ArrayList<>();
    }
}
