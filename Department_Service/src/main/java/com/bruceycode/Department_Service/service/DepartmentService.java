package com.bruceycode.Department_Service.service;

import com.bruceycode.Department_Service.dto.DepartmentDTO;
import com.bruceycode.Department_Service.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department createDepartment(Department department);
    Optional<DepartmentDTO> getDepartmentById(Long id);
    List<DepartmentDTO> getAllDepartments();
    Department updateDepartment(Long id, Department departmentDetails);
    void deleteDepartment(Long id);
}