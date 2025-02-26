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
git clone https://github.com/<your-username>/hospital-management-system.git
cd hospital-management-system