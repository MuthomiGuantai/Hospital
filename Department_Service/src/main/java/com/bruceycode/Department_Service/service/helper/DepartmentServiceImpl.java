package com.bruceycode.Department_Service.service.helper;

import com.bruceycode.Department_Service.dto.DepartmentDTO;
import com.bruceycode.Department_Service.dto.DoctorDTO;
import com.bruceycode.Department_Service.dto.NurseDTO;
import com.bruceycode.Department_Service.model.Department;
import com.bruceycode.Department_Service.repository.DepartmentRepository;
import com.bruceycode.Department_Service.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;
    private final RestTemplate restTemplate;
    private final LoadBalancerClient loadBalancerClient;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, RestTemplate restTemplate, LoadBalancerClient loadBalancerClient) {
        this.departmentRepository = departmentRepository;
        this.restTemplate = restTemplate;
        this.loadBalancerClient = loadBalancerClient;
        logger.info("Initialized with RestTemplate: {}", restTemplate.getClass().getName());
    }

    private void validateHeadOfDepartment(Department department) {
        Long headOfDepartment = department.getHeadOfDepartment();
        List<Long> doctors = department.getDoctors();
        logger.info("Validating headOfDepartment: {} against doctors: {}", headOfDepartment, doctors);
        if (headOfDepartment != null && (doctors == null || !doctors.contains(headOfDepartment))) {
            logger.error("Head of Department {} is not in doctors list {}", headOfDepartment, doctors);
            throw new IllegalArgumentException("Head of Department must be one of the doctors in the department");
        }
    }

    private void validateDoctors(List<Long> doctorIds) {
        if (doctorIds != null) {
            for (Long doctorId : doctorIds) {
                try {
                    ServiceInstance instance = loadBalancerClient.choose("MEDICAL_SERVICE");
                    if (instance == null) {
                        logger.error("No instance available for MEDICAL_SERVICE");
                        throw new IllegalStateException("No MEDICAL_SERVICE instance found in Eureka");
                    }
                    String url = instance.getUri().toString() + "/doctors/" + doctorId;
                    logger.info("Attempting to validate doctor ID {} with resolved URI: {}", doctorId, url);
                    RestTemplate plainRestTemplate = new RestTemplate();
                    DoctorDTO doctor = plainRestTemplate.getForObject(url, DoctorDTO.class);
                    if (doctor == null) {
                        logger.error("No doctor found for ID {} at URI: {}", doctorId, url);
                        throw new IllegalArgumentException("Doctor with ID " + doctorId + " does not exist in medical_service");
                    }
                    logger.info("Validated doctor ID {}: {}", doctorId, doctor);
                } catch (Exception e) {
                    logger.error("Failed to validate doctor ID {}: {} - Stacktrace: {}", doctorId, e.getMessage(), e.getStackTrace());
                    throw new IllegalArgumentException("Doctor with ID " + doctorId + " does not exist in medical_service");
                }
            }
        }
    }

    private void validateNurses(List<Long> nurseIds) {
        if (nurseIds != null) {
            for (Long nurseId : nurseIds) {
                try {
                    ServiceInstance instance = loadBalancerClient.choose("MEDICAL_SERVICE");
                    if (instance == null) {
                        logger.error("No instance available for MEDICAL_SERVICE");
                        throw new IllegalStateException("No MEDICAL_SERVICE instance found in Eureka");
                    }
                    String url = instance.getUri().toString() + "/nurses/" + nurseId;
                    logger.info("Attempting to validate nurse ID {} with resolved URI: {}", nurseId, url);
                    RestTemplate plainRestTemplate = new RestTemplate();
                    NurseDTO nurse = plainRestTemplate.getForObject(url, NurseDTO.class);
                    if (nurse == null) {
                        logger.error("No nurse found for ID {} at URI: {}", nurseId, url);
                        throw new IllegalArgumentException("Nurse with ID " + nurseId + " does not exist in medical_service");
                    }
                    logger.info("Validated nurse ID {}: {}", nurseId, nurse);
                } catch (Exception e) {
                    logger.error("Failed to validate nurse ID {}: {} - Stacktrace: {}", nurseId, e.getMessage(), e.getStackTrace());
                    throw new IllegalArgumentException("Nurse with ID " + nurseId + " does not exist in medical_service");
                }
            }
        }
    }

    private DoctorDTO fetchDoctor(Long doctorId) {
        if (doctorId == null) return null;
        try {
            ServiceInstance instance = loadBalancerClient.choose("MEDICAL_SERVICE");
            if (instance == null) {
                logger.error("No instance available for MEDICAL_SERVICE");
                return null;
            }
            String url = instance.getUri().toString() + "/doctors/" + doctorId;
            DoctorDTO doctor = restTemplate.getForObject(url, DoctorDTO.class);
            logger.info("Fetched doctor with ID {}: {}", doctorId, doctor);
            return doctor;
        } catch (Exception e) {
            logger.error("Failed to fetch doctor with ID {}: {}", doctorId, e.getMessage());
            return null;
        }
    }

    private NurseDTO fetchNurse(Long nurseId) {
        if (nurseId == null) return null;
        try {
            ServiceInstance instance = loadBalancerClient.choose("MEDICAL_SERVICE");
            if (instance == null) {
                logger.error("No instance available for MEDICAL_SERVICE");
                return null;
            }
            String url = instance.getUri().toString() + "/nurses/" + nurseId;
            NurseDTO nurse = restTemplate.getForObject(url, NurseDTO.class);
            logger.info("Fetched nurse with ID {}: {}", nurseId, nurse);
            return nurse;
        } catch (Exception e) {
            logger.error("Failed to fetch nurse with ID {}: {}", nurseId, e.getMessage());
            return null;
        }
    }

    private DepartmentDTO mapToDTO(Department department) {
        DoctorDTO headOfDepartment = fetchDoctor(department.getHeadOfDepartment());
        List<DoctorDTO> doctors = department.getDoctors() != null
                ? department.getDoctors().stream().map(this::fetchDoctor).collect(Collectors.toList())
                : new ArrayList<>();
        List<NurseDTO> nurses = department.getNurses() != null
                ? department.getNurses().stream().map(this::fetchNurse).collect(Collectors.toList())
                : new ArrayList<>();

        return new DepartmentDTO(
                department.getDepartmentId(),
                department.getName(),
                headOfDepartment,
                doctors,
                nurses,
                department.getFacilities()
        );
    }

    @Override
    public Department createDepartment(Department department) {
        logger.info("Creating department: {}", department);
        validateHeadOfDepartment(department);
        validateDoctors(department.getDoctors());
        validateNurses(department.getNurses());
        Department savedDepartment = departmentRepository.save(department);
        logger.info("Successfully created department: {}", savedDepartment);
        return savedDepartment;
    }

    @Override
    public Optional<DepartmentDTO> getDepartmentById(Long id) {
        logger.info("Fetching department by ID: {}", id);
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            DepartmentDTO dto = mapToDTO(department.get());
            logger.info("Successfully fetched department ID {}: {}", id, dto);
            return Optional.of(dto);
        } else {
            logger.warn("No department found for ID: {}", id);
            return Optional.empty();
        }
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        logger.info("Fetching all the departments");
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDTO> dtos = departments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        logger.info("Successfully fetched {} departments", dtos.size());
        return dtos;
    }

    @Override
    public Department updateDepartment(Long id, Department departmentDetails) {
        logger.info("Updating department ID {} with details: {}", id, departmentDetails);
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();
            validateHeadOfDepartment(departmentDetails);
            validateDoctors(departmentDetails.getDoctors());
            department.setName(departmentDetails.getName());
            department.setHeadOfDepartment(departmentDetails.getHeadOfDepartment());
            department.setDoctors(departmentDetails.getDoctors());
            department.setNurses(departmentDetails.getNurses());
            department.setFacilities(departmentDetails.getFacilities());
            Department updatedDepartment = departmentRepository.save(department);
            logger.info("Successfully updated department ID {}: {}", id, updatedDepartment);
            return updatedDepartment;
        }
        logger.error("Department not found for update with ID: {}", id);
        throw new RuntimeException("Department not found with ID " + id);
    }

    @Override
    public void deleteDepartment(Long id) {
        logger.info("Deleting department with ID: {}", id);
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            logger.info("Successfully deleted department with ID: {}", id);
        } else {
            logger.error("Department not found for deletion with ID: {}", id);
            throw new RuntimeException("Department not found with ID " + id);
        }
    }
}