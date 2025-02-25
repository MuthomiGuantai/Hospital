package com.bruceycode.Department_Service.service.helper;

import com.bruceycode.Department_Service.model.Department;
import com.bruceycode.Department_Service.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl {

    @Autowired
    private DepartmentRepository departmentRepository;

    // Create a new department
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    // Get all departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Get a department by ID
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    // Update a department
    public Department updateDepartment(Long id, Department updatedDepartment) {
        return departmentRepository.findById(id)
                .map(department -> {
                    department.setName(updatedDepartment.getName());
                    department.setDept_head(updatedDepartment.getDept_head());
                    department.setStaff(updatedDepartment.getStaff());
                    department.setFacilities(updatedDepartment.getFacilities());
                    return departmentRepository.save(department);
                })
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    // Delete a department
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}