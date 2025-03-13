package com.bruceycode.Department_Service.service;

import com.bruceycode.Department_Service.dto.DepartmentDTO;
import com.bruceycode.Department_Service.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department createDepartment(Department department, String jwtToken);
    Optional<DepartmentDTO> getDepartmentById(Long id, String jwtToken);
    List<DepartmentDTO> getAllDepartments(String jwtToken);
    Department updateDepartment(Long id, Department departmentDetails, String jwtToken);
    void deleteDepartment(Long id);
}