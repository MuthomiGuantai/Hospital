package com.bruceycode.MyHospital.service;

import com.bruceycode.MyHospital.model.entity.Hospital;
import com.bruceycode.MyHospital.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public Optional<Hospital> getHospitalById(Long id) {
        return hospitalRepository.findById(id);
    }

    public Hospital createHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    public Hospital updateHospital(Long id, Hospital hospitalDetails) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospital not found with id " + id));

        hospital.setName(hospitalDetails.getName());
        hospital.setLocation(hospitalDetails.getLocation());
        hospital.setPhone_number(hospitalDetails.getPhone_number());
        hospital.setType(hospitalDetails.getType());
        hospital.setCapacity(hospitalDetails.getCapacity());
        hospital.setFacilities(hospitalDetails.getFacilities());

        return hospitalRepository.save(hospital);
    }

    public void deleteHospital(Long id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospital not found with id " + id));
        hospitalRepository.delete(hospital);
    }
}