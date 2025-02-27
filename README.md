# Hospital Management System

## Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Services](#services)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

The **Hospital Management System** is a microservices-based application designed to manage hospital operations, including medical records, patient data, doctor and nurse information, and department details. Built with **Spring Boot** and **Spring Cloud**, it leverages **Eureka** for service discovery, **Spring Cloud Gateway** for API routing, and **H2** in-memory databases for persistence (configurable for production).

This project demonstrates a distributed system architecture with the following services:
- **Eureka Server**: Service registry for discovery.
- **API Gateway**: Centralized entry point for routing requests.
- **Medical Service**: Manages doctors and patients.
- **Patient Service**: Handles medical records.
- **Department Service**: Manages hospital departments.

---

## Architecture

The system follows a microservices architecture:
- **Eureka Server**: Acts as the service registry, enabling dynamic discovery of services.
- **API Gateway**: Routes client requests to appropriate services using `LoadBalancerGatewayFilter`.
- **Medical Service**: Stores and manages doctor and patient data.
- **Patient Service**: Manages medical records, validating patient and doctor IDs against `medical_service`.
- **Department Service**: Manages departments, including doctors and nurses, with validation against `medical_service`.

### Diagram
+----------------+      +----------------+
|   Client       | ---> | API Gateway    |
| (e.g., curl)   |      | (port: 8100)   |
+----------------+      +----------------+
|
| Eureka Discovery
v
+----------------+      +----------------+      +----------------+
| Medical Service| <--> | Eureka Server   | <--> | Patient Service|
| (port: 8101)   |      | (port: 8761)    |      | (port: 8102)   |
+----------------+      +----------------+      +----------------+
|
v
+----------------+
| Department     |
| Service        |
| (port: 8103)   |
+----------------+

## Prerequisites

- **Java**: JDK 17 or higher
- **Maven**: 3.8.x or higher
- **MySQL**: 8.0 or higher (with a running instance)
- **IDE**: IntelliJ IDEA, VS Code, or Eclipse (optional)
- **curl**: For testing API endpoints (optional)

---

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/MuthomiGuantai/Hospital.git
```
cd hospital-management-system

### 2. Set Up MySQL Database

### Install MySQL
1. Download and install MySQL from [mysql.com](https://www.mysql.com/).
2. Start the MySQL server.

### Create Databases
Log in to MySQL:
   ```bash
mysql -u root -p
```
   ```MySQL
CREATE DATABASE medical_db;
CREATE DATABASE patient_db;
CREATE DATABASE department_db;
```

### 3. Build Each Service
Navigate to each service directory and build with Maven:
   ```bash
cd eureka-server && mvn clean install
cd ../api-gateway && mvn clean install
cd ../medical-service && mvn clean install
cd ../patient-service && mvn clean install
cd ../department-service && mvn clean install
```

### 4. Run the Services
Start each service in separate terminal windows:

 ### Eureka Server
 ```bash
 java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
```

 ### Api Gateway
```bash
 java -jar api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar
```

 ### Medical Service
 ```bash
 java -jar medical-service/target/medical-service-0.0.1-SNAPSHOT.jar
```
 ### Patient Service
 ```bash
 java -jar patient-service/target/patient-service-0.0.1-SNAPSHOT.jar
```
 ### Department Service
 ```bash
 java -jar department-service/target/department-service-0.0.1-SNAPSHOT.jar
```
### 5. Verify Eureka Registration
Open http://localhost:8761 in a browser to see registered services 
(MEDICAL_SERVICE, PATIENT_SERVICE, DEPARTMENT_SERVICE, API-GATEWAY).

## Services
| Service            | Port | Description                          |
|--------------------|------|--------------------------------------|
| Eureka Server      | 8761 | Service registry for discovery       |
| API Gateway        | 8100 | Routes requests to microservices     |
| Medical Service    | 8101 | Manages doctors and patients         |
| Patient Service    | 8102 | Manages medical records              |
| Department Service | 8103 | Manages hospital departments         |

## Usage
- **Access** via Gateway: Use http://localhost:8100/<service-path> to interact with services through the Gateway.
- **Direct** Access: Use http://localhost:<port>/<endpoint> for individual services (e.g., 8103/departments).
- **Test** with curl: See API Endpoints section for examples.

## API Endpoints
### Medical Service (via Gateway: /medical/**)
- **GET** /medical/doctors/{id}: Retrieve a doctor by ID.
```bash
curl -X POST http://localhost:8100/patient/medical-records -H "Content-Type: application/json" -d '{"patientId": 1, "doctorId": 1, "condition": "Flu", "diagnosisDate": "2025-02-25", "notes": "Rest advised"}
```

### Patient Service (via Gateway: /patient/**)
- **POST** /patient/medical-records: Create a medical record.
```bash
curl -X POST http://localhost:8100/patient/medical-records -H "Content-Type: application/json" -d '{"patientId": 1, "doctorId": 1, "condition": "Flu", "diagnosisDate": "2025-02-25", "notes": "Rest advised"}
```

### Department Service (via Gateway: /department/**)
- **POST** /department/departments: Create a department.
```bash
curl -X POST http://localhost:8100/department/departments -H "Content-Type: application/json" -d '{"name": "Cardiology", "headOfDepartment": 1, "doctors": [1, 2], "nurses": [3, 4], "facilities": ["ECG", "X-Ray"]}
```
- **GET** /department/departments/{id}: Retrieve a department by ID.
```bash
curl http://localhost:8100/department/departments/1
```
- **GET** /department/departments: List all departments
```bash
curl http://localhost:8100/department/departments
```