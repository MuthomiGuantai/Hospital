package com.bruceycode.Department_Service.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @Column(nullable = false)
    private String name;

    // Reference to the doctor in charge (must be one of the doctors in the department)
    private Long headOfDepartment;

    // List of doctor IDs in the department
    @ElementCollection
    @CollectionTable(name = "department_doctors", joinColumns = @JoinColumn(name = "department_id"))
    @Column(name = "doctor_id")
    private List<Long> doctors;

    // List of nurse IDs in the department
    @ElementCollection
    @CollectionTable(name = "department_nurses", joinColumns = @JoinColumn(name = "department_id"))
    @Column(name = "nurse_id")
    private List<Long> nurses;

    // List of facilities available in the department
    @ElementCollection
    @CollectionTable(name = "department_facilities", joinColumns = @JoinColumn(name = "department_id"))
    @Column(name = "facility")
    private List<String> facilities;

    // Default constructor
    public Department() {
        this.doctors = new ArrayList<>();
        this.nurses = new ArrayList<>();
        this.facilities = new ArrayList<>();
    }

    // Parameterized constructor
    public Department(Long departmentId, String name, Long headOfDepartment, List<Long> doctors, List<Long> nurses, List<String> facilities) {
        this.departmentId = departmentId;
        this.name = name;
        this.headOfDepartment = headOfDepartment;
        this.doctors = doctors != null ? new ArrayList<>(doctors) : new ArrayList<>();
        this.nurses = nurses != null ? new ArrayList<>(nurses) : new ArrayList<>();
        this.facilities = facilities != null ? new ArrayList<>(facilities) : new ArrayList<>();
    }

    // Getters and Setters
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