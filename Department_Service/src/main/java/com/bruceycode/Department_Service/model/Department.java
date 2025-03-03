package com.bruceycode.Department_Service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @Column(nullable = false)
    private String name;

    private Long headOfDepartment;

    @ElementCollection
    @CollectionTable(name = "department_doctors", joinColumns = @JoinColumn(name = "department_id"))
    @Column(name = "doctor_id")
    private List<Long> doctors;

    @ElementCollection
    @CollectionTable(name = "department_nurses", joinColumns = @JoinColumn(name = "department_id"))
    @Column(name = "nurse_id")
    private List<Long> nurses;

    @ElementCollection
    @CollectionTable(name = "department_facilities", joinColumns = @JoinColumn(name = "department_id"))
    @Column(name = "facility")
    private List<String> facilities;

    public Department() {
        this.doctors = new ArrayList<>();
        this.nurses = new ArrayList<>();
        this.facilities = new ArrayList<>();
    }

    public Department(Long departmentId, String name, Long headOfDepartment, List<Long> doctors, List<Long> nurses, List<String> facilities) {
        this.departmentId = departmentId;
        this.name = name;
        this.headOfDepartment = headOfDepartment;
        this.doctors = doctors != null ? new ArrayList<>(doctors) : new ArrayList<>();
        this.nurses = nurses != null ? new ArrayList<>(nurses) : new ArrayList<>();
        this.facilities = facilities != null ? new ArrayList<>(facilities) : new ArrayList<>();
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(Long headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    public List<Long> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Long> doctors) {
        this.doctors = doctors != null ? new ArrayList<>(doctors) : new ArrayList<>();
    }

    public List<Long> getNurses() {
        return nurses;
    }

    public void setNurses(List<Long> nurses) {
        this.nurses = nurses != null ? new ArrayList<>(nurses) : new ArrayList<>();
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities != null ? new ArrayList<>(facilities) : new ArrayList<>();
    }
}