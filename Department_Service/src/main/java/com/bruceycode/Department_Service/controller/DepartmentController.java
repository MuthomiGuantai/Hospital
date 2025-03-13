package com.bruceycode.Department_Service.controller;

import com.bruceycode.Department_Service.dto.DepartmentDTO;
import com.bruceycode.Department_Service.model.Department;
import com.bruceycode.Department_Service.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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

    @PostMapping("/register")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department,
                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received POST request to create department: {}", department);
        String jwtToken = extractJwtToken(authHeader);
        Department createdDepartment = departmentService.createDepartment(department, jwtToken);
        log.info("Successfully created department: {}", createdDepartment);
        return ResponseEntity.ok(createdDepartment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id,
                                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received GET request for department ID: {}", id);
        String jwtToken = extractJwtToken(authHeader);
        Optional<DepartmentDTO> department = departmentService.getDepartmentById(id, jwtToken);
        if (department.isPresent()) {
            log.info("Successfully retrieved department ID {}: {}", id, department.get());
            return ResponseEntity.ok(department.get());
        } else {
            log.warn("Department not found for ID: {}", id);
            throw new RuntimeException("Department not found for ID: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received GET request for all departments");
        String jwtToken = extractJwtToken(authHeader);
        List<DepartmentDTO> departments = departmentService.getAllDepartments(jwtToken);
        log.info("Successfully retrieved {} departments", departments.size());
        return ResponseEntity.ok(departments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id,
                                                       @RequestBody Department departmentDetails,
                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received PUT request to update department ID {} with details: {}", id, departmentDetails);
        String jwtToken = extractJwtToken(authHeader);
        Department updatedDepartment = departmentService.updateDepartment(id, departmentDetails, jwtToken);
        log.info("Successfully updated department ID {}: {}", updatedDepartment);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        log.info("Received DELETE request for department ID: {}", id);
        departmentService.deleteDepartment(id);
        log.info("Successfully deleted department ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    private String extractJwtToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer " prefix
        }
        log.error("Missing or invalid Authorization header: {}", authHeader);
        throw new IllegalArgumentException("Missing or invalid Authorization header");
    }
}