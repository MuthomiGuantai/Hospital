package com.bruceycode.Medical_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MedicalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalServiceApplication.class, args);
	}

}
