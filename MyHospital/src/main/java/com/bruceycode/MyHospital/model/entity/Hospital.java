package com.bruceycode.MyHospital.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hospitals")
public class Hospital {

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
    private String location;
    private Integer phone_number;
    private String type;
    private Integer capacity;
    private String facilities;

    public Hospital() {
    }

    public Hospital(String name, String location, Integer phone_number, String type, Integer capacity, String facilities) {
        this.name = name;
        this.location = location;
        this.phone_number = phone_number;
        this.type = type;
        this.capacity = capacity;
        this.facilities = facilities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", phone_number=" + phone_number +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", facilities='" + facilities + '\'' +
                '}';
    }
}
