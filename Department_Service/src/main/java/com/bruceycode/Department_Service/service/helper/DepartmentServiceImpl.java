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
        this.restTemplate = restTemplate; // Non-@LoadBalanced is fine here
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
                    // Use plain RestTemplate call without LoadBalancer interference
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
        validateHeadOfDepartment(department);
        validateDoctors(department.getDoctors());
        return departmentRepository.save(department);
    }

    @Override
    public Optional<DepartmentDTO> getDepartmentById(Long id) {
        return departmentRepository.findById(id).map(this::mapToDTO);
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Department updateDepartment(Long id, Department departmentDetails) {
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