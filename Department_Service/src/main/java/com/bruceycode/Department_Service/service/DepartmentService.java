package com.bruceycode.Department_Service.service;

import com.bruceycode.Department_Service.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department createDepartment(Department department);
    Optional<Department> getDepartmentById(Long id);
    List<Department> getAllDepartments();
    Department updateDepartment(Long id, Department departmentDetails);
    void deleteDepartment(Long id);
}