package com.bruceycode.Medical_Service.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nurses")
@Data
public class Nurse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nurseId;

    @Column(nullable = false)
    private String name;

    @Column
    private String department;

    @Column
    private String contactPhone;

    @Column
    private String contactEmail;

    @Column(columnDefinition = "TEXT") // For storing JSON or complex shift data
    private String shiftSchedule; // Could use a separate entity for complex schedules

    // Many-to-many with Patient
    @ManyToMany
    @JoinTable(
            name = "nurse_patient",
            joinColumns = @JoinColumn(name = "nurse_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    @JsonManagedReference("patient-nurse")
    private List<Patient> patients = new ArrayList<>();

    // Constructors
    public Nurse() {}

    public Nurse(String name, String department, String contactPhone,
                 String contactEmail, String shiftSchedule) {
        this.name = name;
        this.department = department;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.shiftSchedule = shiftSchedule;
    }

    // Getters and Setters
    public Long getNurseId() { return nurseId; }
    public void setNurseId(Long nurseId) { this.nurseId = nurseId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getShiftSchedule() { return shiftSchedule; }
    public void setShiftSchedule(String shiftSchedule) { this.shiftSchedule = shiftSchedule; }
    public List<Patient> getPatients() { return patients; }
    public void setPatients(List<Patient> patients) { this.patients = patients; }

    // Helper method for bidirectional relationship
    public void addPatient(Patient patient) {
        patients.add(patient);
        patient.getNurses().add(this);
    }
}