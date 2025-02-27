package com.bruceycode.Medical_Service.model.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @Column(nullable = false)
    private String name;

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

    @Column(columnDefinition = "TEXT") // For storing JSON or complex schedule data
    private String schedule; // Could use a separate entity for complex schedules

    // Many-to-many with Patient
    @ManyToMany
    @JoinTable(
            name = "doctor_patient",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    @JsonManagedReference("patient-doctor")
    private List<Patient> patients = new ArrayList<>();

    // Constructors
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

    // Getters and Setters
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getOfficeLocation() { return officeLocation; }
    public void setOfficeLocation(String officeLocation) { this.officeLocation = officeLocation; }
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    public List<Patient> getPatients() { return patients; }
    public void setPatients(List<Patient> patients) { this.patients = patients; }

    // Helper method for bidirectional relationship
    public void addPatient(Patient patient) {
        patients.add(patient);
        patient.getDoctors().add(this);
    }
}