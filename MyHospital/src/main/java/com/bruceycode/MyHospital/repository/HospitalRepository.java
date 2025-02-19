package com.bruceycode.MyHospital.repository;

import com.bruceycode.MyHospital.model.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository <Hospital, Long> {

}
