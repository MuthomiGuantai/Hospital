package com.bruceycode.Department_Service.repository;

import com.bruceycode.Department_Service.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
