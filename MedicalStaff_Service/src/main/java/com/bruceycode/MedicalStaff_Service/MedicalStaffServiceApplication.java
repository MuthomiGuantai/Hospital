package com.bruceycode.MedicalStaff_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MedicalStaffServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalStaffServiceApplication.class, args);
	}

}
