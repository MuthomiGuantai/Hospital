package com.bruceycode.Medical_Service.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "nurses")
public class Nurse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nurseId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String department;

    @Column
    private String contactPhone;

    @Column
    private String contactEmail;

    @Column(columnDefinition = "TEXT")
    private String shiftSchedule;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "nurse_patient",
            joinColumns = @JoinColumn(name = "nurse_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    @JsonManagedReference("nurse-patient")
    private List<Patient> patients = new ArrayList<>();

    public Nurse() {}

    public Nurse(String name, String department, String contactPhone,
                 String contactEmail, String shiftSchedule) {
        this.name = name;
        this.department = department;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.shiftSchedule = shiftSchedule;
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
        patient.getNurses().add(this);
    }
}