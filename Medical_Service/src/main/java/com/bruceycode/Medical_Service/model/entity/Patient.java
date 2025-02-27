package com.bruceycode.Medical_Service.model.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @Column(nullable = false)
    private String name;

    @Column
    private String gender;

    @Column
    private String email;

    @Column
    private Integer phone_number;

    @Column
    private LocalDate dob;

    @ManyToMany(mappedBy = "patients")
    @JsonBackReference("patient-doctor")
    private List<Doctor> doctors = new ArrayList<>();


    @ManyToMany(mappedBy = "patients")
    @JsonBackReference("patient-nurse")
    private List<Nurse> nurses = new ArrayList<>();

    public Patient() {}

    public Patient(String name, String gender, String email, Integer phone_number, LocalDate dob) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone_number = phone_number;
        this.dob = dob;
    }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() {return gender;}
    public void setGender(String gender) {this.gender = gender;}
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getPhone_number() {return phone_number;}
    public void setPhone_number(Integer phone_number) {this.phone_number = phone_number;}
    public LocalDate getDob() {return dob;}
    public void setDob(LocalDate dob) {this.dob = dob;}
    public List<Doctor> getDoctors() { return doctors; }
    public void setDoctors(List<Doctor> doctors) { this.doctors = doctors; }
    public List<Nurse> getNurses() { return nurses; }
    public void setNurses(List<Nurse> nurses) { this.nurses = nurses; }

    // Helper methods for bidirectional relationship
    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
        doctor.getPatients().add(this);
    }

    public void addNurse(Nurse nurse) {
        nurses.add(nurse);
        nurse.getPatients().add(this);
    }
}
