package com.bruceycode.Department_Service.controller;

import com.bruceycode.Department_Service.dto.DepartmentDTO;
import com.bruceycode.Department_Service.model.Department;
import com.bruceycode.Department_Service.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
        logger.info("DepartmentController initialized with DepartmentService");
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        logger.info("Received POST request to create department: {}", department);
        try {
            Department createdDepartment = departmentService.createDepartment(department);
            logger.info("Successfully created department: {}", createdDepartment);
            return ResponseEntity.ok(createdDepartment);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create department due to validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
        logger.info("Received GET request for department ID: {}", id);
        Optional<DepartmentDTO> department = departmentService.getDepartmentById(id);
        if (department.isPresent()) {
            logger.info("Successfully retrieved department ID {}: {}", id, department.get());
            return ResponseEntity.ok(department.get());
        } else {
            logger.warn("Department not found for ID: {}", id);
            throw new RuntimeException("Department not found for ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        logger.info("Received GET request for all departments");
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        logger.info("Successfully retrieved {} departments", departments.size());
        return ResponseEntity.ok(departments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department departmentDetails) {
        logger.info("Received PUT request to update department ID {} with details: {}", id, departmentDetails);
        try {
            Department updatedDepartment = departmentService.updateDepartment(id, departmentDetails);
            logger.info("Successfully updated department ID {}: {}", id, updatedDepartment);
            return ResponseEntity.ok(updatedDepartment);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update department ID {} due to validation error: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            logger.warn("Department not found for update with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        logger.info("Received DELETE request for department ID: {}", id);
        try {
            departmentService.deleteDepartment(id);
            logger.info("Successfully deleted department ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.warn("Department not found for deletion with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}