package com.bruceycode.Medical_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication(scanBasePackages = "com.bruceycode.Medical_Service")
@EnableDiscoveryClient
public class MedicalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalServiceApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner loadData(UserRepository userRepository, DoctorRepository doctorRepository,
									  NurseRepository nurseRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Users
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setRole(UserRole.ADMIN);
			userRepository.save(admin);

			User doctorUser = new User();
			doctorUser.setUsername("doctorA");
			doctorUser.setPassword(passwordEncoder.encode("doctor123"));
			doctorUser.setRole(UserRole.DOCTOR);
			userRepository.save(doctorUser);

			User nurseUser = new User();
			nurseUser.setUsername("nurseA");
			nurseUser.setPassword(passwordEncoder.encode("nurse123"));
			nurseUser.setRole(UserRole.NURSE);
			userRepository.save(nurseUser);

			// Doctors
			Doctor doctor = new Doctor();
			doctor.setName("Dr. Bruce Muthomi");
			doctor.setUsername("doctorA");
			doctor.setSpecialization("Orthopediology");
			doctor.setDepartment("Orthopedic Center");
			doctor.setContactPhone("0791890480");
			doctor.setContactEmail("bruce.muthomi@hospital.com");
			doctor.setSchedule("{\\\"Monday\\\": \\\"9:00 AM - 5:00 PM\\\", \\\"Tuesday\\\": \\\"10:00 AM - 6:00 PM\\\"}");
			doctor.setOfficeLocation("Room 102, Building A");
			doctorRepository.save(doctor);

			// Nurses
			Nurse nurse = new Nurse();
			nurse.setName("Violet Wanjiru");
			nurse.setUsername("nurseA");
			nurse.setDepartment("Orthopedic Center");
			nurse.setContactEmail("violet.wanjiru@hospital.com");
			nurse.setContactPhone("0708705439");
			nurse.setShiftSchedule("Morning Shift");
			nurseRepository.save(nurse);
		};
	}*/ //creating startup users for testing
}
