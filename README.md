# Hospital Management System

## Overview
The Hospital Management System is a microservices-based application built using Spring Boot 3 and MySQL. It is designed to manage hospital operations efficiently, including patient records, medical staff details, and department information. The system is composed of four microservices:

1. **my_hospital**: The main service that acts as the entry point for the system.
2. **medicalstaff_service**: Manages information about medical staff, including doctors, nurses, and other healthcare professionals.
3. **patient_service**: Handles patient records, including personal details, medical history, and appointments.
4. **department_service**: Manages hospital departments, such as Cardiology, Radiology, and Emergency.

The system uses **Eureka** for service discovery and **Zipkin** for distributed tracing to monitor and debug the microservices.

---

## Technologies Used
- **Spring Boot 3**: Framework for building the microservices.
- **MySQL**: Database for storing application data.
- **Eureka**: Service discovery server for managing microservices.
- **Zipkin**: Distributed tracing system for monitoring requests across microservices.
- **Spring Cloud**: For microservices communication and configuration.
- **Maven**: Build tool for managing dependencies.

---

## Microservices Architecture
The system is divided into four microservices:

### 1. my_hospital
- Entry point for the application.
- Handles API gateway responsibilities.
- Routes requests to the appropriate microservices.

### 2. medicalstaff_service
- Manages medical staff information.
- CRUD operations for doctors, nurses, and other staff.
- Endpoints for assigning staff to departments.

### 3. patient_service
- Manages patient records.
- CRUD operations for patient details.
- Tracks patient appointments and medical history.

### 4. department_service
- Manages hospital departments.
- CRUD operations for department details.
- Tracks department-specific staff and patients.

---

## Service Discovery with Eureka
The system uses Eureka as the service discovery server. Each microservice registers itself with Eureka, allowing other services to locate and communicate with it dynamically.

- **Eureka Server URL**: [http://localhost:8761](http://localhost:8761)

---

## Distributed Tracing with Zipkin
Zipkin is used for distributed tracing to monitor and debug requests as they flow through the microservices.

- **Zipkin Server URL**: [http://localhost:9411](http://localhost:9411)

---

## Database Schema
Each microservice has its own database schema in MySQL. The schemas are designed to be independent and scalable.

### my_hospital
- Stores gateway-related configurations.

### medicalstaff_service
- Tables: `medical_staff`, `staff_department_mapping`.

### patient_service
- Tables: `patients`, `appointments`, `medical_history`.

### department_service
- Tables: `departments`, `department_staff_mapping`.

---

## Getting Started

### Prerequisites
- Java 17 or higher.
- MySQL 8.x.
- Maven 3.x.
- Docker (optional, for running Zipkin).

### Installation
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/hospital-management-system.git
   cd hospital-management-system
