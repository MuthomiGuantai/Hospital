package com.bruceycode.Medical_Service.model.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String specialization;

    @Column
    private String department;

    @Column
    private String contactPhone;

    @Column
    private String contactEmail;

    @Column
    private String officeLocation;

    @Column(columnDefinition = "TEXT")
    private String schedule;


    @ManyToMany
    @JoinTable(
            name = "doctor_patient",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    /*@JsonManagedReference("patient-doctor")*/
    private List<Patient> patients = new ArrayList<>();

    public Doctor() {}

    public Doctor(String name, String specialization, String department,
                  String contactPhone, String contactEmail, String officeLocation,
                  String schedule) {
        this.name = name;
        this.specialization = specialization;
        this.department = department;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.officeLocation = officeLocation;
        this.schedule = schedule;
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
        patient.getDoctors().add(this);
    }
}