package com.bruceycode.MyHospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MyHospitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyHospitalApplication.class, args);
	}

}
