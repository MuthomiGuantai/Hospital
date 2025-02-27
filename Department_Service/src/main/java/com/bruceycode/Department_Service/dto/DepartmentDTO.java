package com.bruceycode.Department_Service.dto;

import java.util.List;

public class DepartmentDTO {
    private Long departmentId;
    private String name;
    private DoctorDTO headOfDepartment;
    private List<DoctorDTO> doctors;
    private List<NurseDTO> nurses;
    private List<String> facilities;

    public DepartmentDTO() {}


    public DepartmentDTO(Long departmentId, String name, DoctorDTO headOfDepartment, List<DoctorDTO> doctors, List<NurseDTO> nurses, List<String> facilities) {
        this.departmentId = departmentId;
        this.name = name;
        this.headOfDepartment = headOfDepartment;
        this.doctors = doctors;
        this.nurses = nurses;
        this.facilities = facilities;
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

    public DoctorDTO getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(DoctorDTO headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }

    public List<DoctorDTO> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorDTO> doctors) {
        this.doctors = doctors;
    }

    public List<NurseDTO> getNurses() {
        return nurses;
    }

    public void setNurses(List<NurseDTO> nurses) {
        this.nurses = nurses;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }
}