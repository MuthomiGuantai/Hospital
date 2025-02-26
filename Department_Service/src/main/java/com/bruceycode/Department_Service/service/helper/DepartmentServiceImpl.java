package com.bruceycode.Department_Service.service.helper;

import com.bruceycode.Department_Service.model.Department;
import com.bruceycode.Department_Service.repository.DepartmentRepository;
import com.bruceycode.Department_Service.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    private void validateHeadOfDepartment(Department department) {
        Long headOfDepartment = department.getHeadOfDepartment();
        List<Long> doctors = department.getDoctors();
        if (headOfDepartment != null && (doctors == null || !doctors.contains(headOfDepartment))) {
            throw new IllegalArgumentException("Head of Department must be one of the doctors in the department");
        }
    }

    @Override
    public Department createDepartment(Department department) {
        validateHeadOfDepartment(department);
        return departmentRepository.save(department);
    }

    @Override
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department updateDepartment(Long id, Department departmentDetails) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();
            validateHeadOfDepartment(departmentDetails);
            department.setName(departmentDetails.getName());
            department.setHeadOfDepartment(departmentDetails.getHeadOfDepartment());
            department.setDoctors(departmentDetails.getDoctors());
            department.setNurses(departmentDetails.getNurses());
            department.setFacilities(departmentDetails.getFacilities());
            return departmentRepository.save(department);
        }
        throw new RuntimeException("Department not found with ID " + id);
    }

    @Override
    public void deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Department not found with ID " + id);
        }
    }
}