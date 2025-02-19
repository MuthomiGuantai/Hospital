package com.bruceycode.MedicalStaff_Service.controller;

import com.bruceycode.MedicalStaff_Service.model.Pharmacist;
import com.bruceycode.MedicalStaff_Service.service.PharmacistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacists")
public class PharmacistController {

    @Autowired
    private PharmacistService pharmacistService;

    // Create a new pharmacist
    @PostMapping("/register")
    public Pharmacist createPharmacist(@RequestBody Pharmacist pharmacist) {
        return pharmacistService.createPharmacist(pharmacist);
    }

    // Get all pharmacists
    @GetMapping
    public List<Pharmacist> getAllPharmacists() {
        return pharmacistService.getAllPharmacists();
    }

    // Get a pharmacist by ID
    @GetMapping("/{id}")
    public ResponseEntity<Pharmacist> getPharmacistById(@PathVariable Long id) {
        return pharmacistService.getPharmacistById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a pharmacist
    @PutMapping("/{id}")
    public ResponseEntity<Pharmacist> updatePharmacist(@PathVariable Long id, @RequestBody Pharmacist pharmacistDetails) {
        Pharmacist updatedPharmacist = pharmacistService.updatePharmacist(id, pharmacistDetails);
        return ResponseEntity.ok(updatedPharmacist);
    }

    // Delete a pharmacist
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePharmacist(@PathVariable Long id) {
        pharmacistService.deletePharmacist(id);
        return ResponseEntity.noContent().build();
    }
}