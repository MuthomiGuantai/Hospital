package com.bruceycode.Patient_Service.model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

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
    private LocalDate dob;
    private String gender;
    private Integer phone_number;
    private String email;
    private String medical_history;

    public Patient() {
    }

    public Patient(String name, LocalDate dob, String gender, Integer phone_number, String medical_history, String email) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.phone_number = phone_number;
        this.medical_history = medical_history;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getMedical_history() {
        return medical_history;
    }

    public void setMedical_history(String medical_history) {
        this.medical_history = medical_history;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", phone_number=" + phone_number +
                ", email='" + email + '\'' +
                ", medical_history='" + medical_history + '\'' +
                '}';
    }
}
