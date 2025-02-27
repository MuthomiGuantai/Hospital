package com.bruceycode.Medical_Service.repository;

import com.bruceycode.Medical_Service.model.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NurseRepository extends JpaRepository<Nurse, Long> {
}
