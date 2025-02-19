package com.bruceycode.MedicalStaff_Service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nurses")
public class Nurse {

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
    private String department;
    private String patients;
    private String email;
    private Integer phone_number;

    public Nurse() {
    }

    public Nurse(String name, String department, String patients, String email, Integer phone_number) {
        this.name = name;
        this.department = department;
        this.patients = patients;
        this.email = email;
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPatients() {
        return patients;
    }

    public void setPatients(String patients) {
        this.patients = patients;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return "Nurse{" +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", patients='" + patients + '\'' +
                ", email='" + email + '\'' +
                ", phone_number=" + phone_number +
                '}';
    }
}
