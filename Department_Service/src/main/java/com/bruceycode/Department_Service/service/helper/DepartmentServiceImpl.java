package com.bruceycode.Department_Service.service.helper;

import com.bruceycode.Department_Service.dto.DepartmentDTO;
import com.bruceycode.Department_Service.dto.DoctorDTO;
import com.bruceycode.Department_Service.dto.NurseDTO;
import com.bruceycode.Department_Service.model.Department;
import com.bruceycode.Department_Service.repository.DepartmentRepository;
import com.bruceycode.Department_Service.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final RestTemplate restTemplate;

    private static final String MEDICAL_SERVICE_URL = "http://medical-service:8101";

    private void validateHeadOfDepartment(Department department) {
        Long headOfDepartment = department.getHeadOfDepartment();
        List<Long> doctors = department.getDoctors();
        log.info("Validating headOfDepartment: {} against doctors: {}", headOfDepartment, doctors);
        if (headOfDepartment != null && (doctors == null || !doctors.contains(headOfDepartment))) {
            log.error("Head of Department {} is not in doctors list {}", headOfDepartment, doctors);
            throw new IllegalArgumentException("Head of Department must be one of the doctors in the department");
        }
    }

    private void validateDoctors(List<Long> doctorIds, String jwtToken) {
        if (doctorIds != null) {
            for (Long doctorId : doctorIds) {
                String url = MEDICAL_SERVICE_URL + "/doctors/" + doctorId;
                log.info("Attempting to validate doctor ID {} with resolved URI: {}", doctorId, url);
                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + jwtToken);
                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    ResponseEntity<DoctorDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, DoctorDTO.class);
                    DoctorDTO doctor = response.getBody();
                    if (doctor == null) {
                        log.error("No doctor found for ID {} at URI: {}", doctorId, url);
                        throw new IllegalArgumentException("Doctor with ID " + doctorId + " does not exist in medical_service");
                    }
                    log.info("Validated doctor ID {}: {}", doctorId, doctor);
                } catch (HttpClientErrorException e) {
                    log.error("Failed to validate doctor ID {}: {} on GET request for \"{}\": [no body]",
                            doctorId, e.getStatusCode(), url);
                    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                        throw new IllegalArgumentException("Doctor with ID " + doctorId + " does not exist in medical_service");
                    } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                        throw new SecurityException("Access denied to medical_service for doctor ID " + doctorId + ": 403 Forbidden");
                    } else {
                        throw new RuntimeException("Failed to validate doctor ID " + doctorId + ": " + e.getStatusCode());
                    }
                }
            }
        }
    }

    private void validateNurses(List<Long> nurseIds, String jwtToken) {
        if (nurseIds != null) {
            for (Long nurseId : nurseIds) {
                String url = MEDICAL_SERVICE_URL + "/nurses/" + nurseId;
                log.info("Attempting to validate nurse ID {} with resolved URI: {}", nurseId, url);
                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + jwtToken);
                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    ResponseEntity<NurseDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, NurseDTO.class);
                    NurseDTO nurse = response.getBody();
                    if (nurse == null) {
                        log.error("No nurse found for ID {} at URI: {}", nurseId, url);
                        throw new IllegalArgumentException("Nurse with ID " + nurseId + " does not exist in medical_service");
                    }
                    log.info("Validated nurse ID {}: {}", nurseId, nurse);
                } catch (HttpClientErrorException e) {
                    log.error("Failed to validate nurse ID {}: {} on GET request for \"{}\": [no body]",
                            nurseId, e.getStatusCode(), url);
                    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                        throw new IllegalArgumentException("Nurse with ID " + nurseId + " does not exist in medical_service");
                    } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                        throw new SecurityException("Access denied to medical_service for nurse ID " + nurseId + ": 403 Forbidden");
                    } else {
                        throw new RuntimeException("Failed to validate nurse ID " + nurseId + ": " + e.getStatusCode());
                    }
                }
            }
        }
    }

    private DoctorDTO fetchDoctor(Long doctorId, String jwtToken) {
        if (doctorId == null) return null;
        String url = MEDICAL_SERVICE_URL + "/doctors/" + doctorId;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<DoctorDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, DoctorDTO.class);
            DoctorDTO doctor = response.getBody();
            log.info("Fetched doctor with ID {}: {}", doctorId, doctor);
            return doctor;
        } catch (Exception e) {
            log.error("Failed to fetch doctor with ID {}: {}", doctorId, e.getMessage());
            return null;
        }
    }

    private NurseDTO fetchNurse(Long nurseId, String jwtToken) {
        if (nurseId == null) return null;
        String url = MEDICAL_SERVICE_URL + "/nurses/" + nurseId;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<NurseDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, NurseDTO.class);
            NurseDTO nurse = response.getBody();
            log.info("Fetched nurse with ID {}: {}", nurseId, nurse);
            return nurse;
        } catch (Exception e) {
            log.error("Failed to fetch nurse with ID {}: {}", nurseId, e.getMessage());
            return null;
        }
    }

    private DepartmentDTO mapToDTO(Department department, String jwtToken) {
        DoctorDTO headOfDepartment = fetchDoctor(department.getHeadOfDepartment(), jwtToken);
        List<DoctorDTO> doctors = department.getDoctors() != null
                ? department.getDoctors().stream().map(id -> fetchDoctor(id, jwtToken)).collect(Collectors.toList())
                : new ArrayList<>();
        List<NurseDTO> nurses = department.getNurses() != null
                ? department.getNurses().stream().map(id -> fetchNurse(id, jwtToken)).collect(Collectors.toList())
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
    public Department createDepartment(Department department, String jwtToken) {
        log.info("Creating department: {}", department);
        validateHeadOfDepartment(department);
        validateDoctors(department.getDoctors(), jwtToken);
        validateNurses(department.getNurses(), jwtToken);
        Department savedDepartment = departmentRepository.save(department);
        log.info("Successfully created department: {}", savedDepartment);
        return savedDepartment;
    }

    @Override
    public Optional<DepartmentDTO> getDepartmentById(Long id, String jwtToken) {
        log.info("Fetching department by ID: {}", id);
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            DepartmentDTO dto = mapToDTO(department.get(), jwtToken);
            log.info("Successfully fetched department ID {}: {}", id, dto);
            return Optional.of(dto);
        } else {
            log.warn("No department found for ID: {}", id);
            return Optional.empty();
        }
    }

    @Override
    public List<DepartmentDTO> getAllDepartments(String jwtToken) {
        log.info("Fetching all the departments");
        List<Department> departments = departmentRepository.findAll();
        List<DepartmentDTO> dtos = departments.stream()
                .map(department -> mapToDTO(department, jwtToken))
                .collect(Collectors.toList());
        log.info("Successfully fetched {} departments", dtos.size());
        return dtos;
    }

    @Override
    public Department updateDepartment(Long id, Department departmentDetails, String jwtToken) {
        log.info("Updating department ID {} with details: {}", id, departmentDetails);
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();
            validateHeadOfDepartment(departmentDetails);
            validateDoctors(departmentDetails.getDoctors(), jwtToken);
            validateNurses(departmentDetails.getNurses(), jwtToken);
            department.setName(departmentDetails.getName());
            department.setHeadOfDepartment(departmentDetails.getHeadOfDepartment());
            department.setDoctors(departmentDetails.getDoctors());
            department.setNurses(departmentDetails.getNurses());
            department.setFacilities(departmentDetails.getFacilities());
            Department updatedDepartment = departmentRepository.save(department);
            log.info("Successfully updated department ID {}: {}", id, updatedDepartment);
            return updatedDepartment;
        }
        log.error("Department not found for update with ID: {}", id);
        throw new RuntimeException("Department not found with ID " + id);
    }

    @Override
    public void deleteDepartment(Long id) {
        log.info("Deleting department with ID: {}", id);
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            log.info("Successfully deleted department with ID: {}", id);
        } else {
            log.error("Department not found for deletion with ID: {}", id);
            throw new RuntimeException("Department not found with ID " + id);
        }
    }
}