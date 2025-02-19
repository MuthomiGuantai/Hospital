package com.bruceycode.Patient_Service.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "medicalrecord")
public class MedicalRecord {

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
    private Long patient_id;
    private Long doctor_id;
    private String diagnosis;
    private LocalDate date;
    private String medication;
    private String test_results;

    public MedicalRecord() {
    }

    public MedicalRecord(Long patient_id, Long doctor_id, String diagnosis, LocalDate date, String medication, String test_results) {
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.diagnosis = diagnosis;
        this.date = date;
        this.medication = medication;
        this.test_results = test_results;
    }

    public Long getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Long patient_id) {
        this.patient_id = patient_id;
    }

    public Long getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Long doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getTest_results() {
        return test_results;
    }

    public void setTest_results(String test_results) {
        this.test_results = test_results;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "patient_id=" + patient_id +
                ", doctor_id=" + doctor_id +
                ", diagnosis='" + diagnosis + '\'' +
                ", date=" + date +
                ", medication='" + medication + '\'' +
                ", test_results='" + test_results + '\'' +
                '}';
    }
}
