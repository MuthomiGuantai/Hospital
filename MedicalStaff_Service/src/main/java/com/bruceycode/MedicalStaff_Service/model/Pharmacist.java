package com.bruceycode.MedicalStaff_Service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pharmacists")
public class Pharmacist {

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
    private String email;
    private Integer phone_number;

    public Pharmacist() {
    }

    public Pharmacist(String name, String email, Integer phone_number) {
        this.name = name;
        this.email = email;
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Pharmacist{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone_number=" + phone_number +
                '}';
    }
}
