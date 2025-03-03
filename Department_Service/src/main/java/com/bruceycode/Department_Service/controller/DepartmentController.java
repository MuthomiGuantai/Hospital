package com.bruceycode.Department_Service.controller;

import com.bruceycode.Department_Service.dto.DepartmentDTO;
import com.bruceycode.Department_Service.model.Department;
import com.bruceycode.Department_Service.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/departments")
public class DepartmentController {


    private final DepartmentService departmentService;


    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        log.info("Received POST request to create department: {}", department);
        Department createdDepartment = departmentService.createDepartment(department);
        log.info("Successfully created department: {}", createdDepartment);
        return ResponseEntity.ok(createdDepartment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
        log.info("Received GET request for department ID: {}", id);
        Optional<DepartmentDTO> department = departmentService.getDepartmentById(id);
        if (department.isPresent()) {
            log.info("Successfully retrieved department ID {}: {}", id, department.get());
            return ResponseEntity.ok(department.get());
        } else {
            log.warn("Department not found for ID: {}", id);
            throw new RuntimeException("Department not found for ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        log.info("Received GET request for all departments");
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        log.info("Successfully retrieved {} departments", departments.size());
        return ResponseEntity.ok(departments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department departmentDetails) {
        log.info("Received PUT request to update department ID {} with details: {}", id, departmentDetails);
        Department updatedDepartment = departmentService.updateDepartment(id, departmentDetails);
        log.info("Successfully updated department ID {}: {}", id, updatedDepartment);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        log.info("Received DELETE request for department ID: {}", id);
        departmentService.deleteDepartment(id);
        log.info("Successfully deleted department ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}