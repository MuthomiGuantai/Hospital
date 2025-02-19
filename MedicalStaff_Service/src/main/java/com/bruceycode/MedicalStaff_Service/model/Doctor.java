package com.bruceycode.MedicalStaff_Service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @SequenceGenerator(
            name = "hospital_sequence",
            sequenceName = "hospital_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "hospital_sequence"
    )

    private Long id;
    private String name;
    private String specialization;
    private String department;
    private Integer phone_number;
    private String email;
    private String patients;

    public Doctor() {
    }

    public Doctor(String name, String department, String specialization, Integer phone_number, String email, String patients) {
        this.name = name;
        this.department = department;
        this.specialization = specialization;
        this.phone_number = phone_number;
        this.email = email;
        this.patients = patients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPatients() {
        return patients;
    }

    public void setPatients(String patients) {
        this.patients = patients;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", department='" + department + '\'' +
                ", phone_number=" + phone_number +
                ", email='" + email + '\'' +
                ", patients='" + patients + '\'' +
                '}';
    }
}
