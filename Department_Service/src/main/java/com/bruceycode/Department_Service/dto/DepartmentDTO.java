package com.bruceycode.Department_Service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
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

}