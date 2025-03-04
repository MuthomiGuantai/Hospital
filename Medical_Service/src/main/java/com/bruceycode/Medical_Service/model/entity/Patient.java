package com.bruceycode.Medical_Service.model.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "patients")
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
    /*@JsonBackReference("nurse-patient")*/
    private List<Nurse> nurses = new ArrayList<>();

    public Patient() {}

    public Patient(String name, String gender, String email, Integer phone_number, LocalDate dob) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone_number = phone_number;
        this.dob = dob;
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
        doctor.getPatients().add(this);
    }

    public void addNurse(Nurse nurse) {
        nurses.add(nurse);
        nurse.getPatients().add(this);
    }
}
