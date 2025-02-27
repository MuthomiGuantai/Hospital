package com.bruceycode.Medical_Service.repository;

import com.bruceycode.Medical_Service.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository <Doctor, Long> {
}
