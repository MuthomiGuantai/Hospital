pipeline {
    agent any
    tools {
        maven 'Maven3' // Ensure this matches the name in Jenkins config
    }
    environment {
        // Define ports for verification
        SERVICE_REGISTRY_PORT = '8761'
        CONFIG_SERVER_PORT = '8104'
        API_GATEWAY_PORT = '8100'
        MEDICAL_SERVICE_PORT = '8101'
        PATIENT_SERVICE_PORT = '8102'
        DEPARTMENT_SERVICE_PORT = '8103'
    }
    stages {
        stage('Checkout') {
            steps {
                // Clone the repository
                git branch: 'main', url: 'https://github.com/MuthomiGuantai/Hospital'
            }
        }
        stage('Build All') {
            parallel {
                stage('Build Service Registry') {
                    steps {
                        dir('Service_Registry') {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Build Config Server') {
                    steps {
                        dir('Config_Server') {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Build API Gateway') {
                    steps {
                        dir('Api_Gateway') {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Build Medical Service') {
                    steps {
                        dir('Medical-Service') {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Build Patient Service') {
                    steps {
                        dir('Patient-Service') {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
                stage('Build Department Service') {
                    steps {
                        dir('Department-Service') {
                            sh 'mvn clean package -DskipTests'
                        }
                    }
                }
            }
        }
        stage('Deploy Dependencies') {
            steps {
                // Deploy Service Registry (Eureka) first
                dir('Service-Registry') {
                    sh 'java -jar target/Service_Registry-0.0.1-SNAPSHOT.jar &'
                    sleep 20 // Wait for Eureka to start
                }
                // Deploy Config Server next
                dir('Config-Server') {
                    sh 'java -jar target/Config_Server-0.0.1-SNAPSHOT.jar &'
                    sleep 20 // Wait for Config Server to start
                }
            }
        }
        stage('Deploy Services') {
            parallel {
                stage('Deploy API Gateway') {
                    steps {
                        dir('Api_Gateway') {
                            sh 'java -jar target/Api_Gateway-0.0.1-SNAPSHOT.jar &'
                            sleep 20
                        }
                    }
                }
                stage('Deploy Medical Service') {
                    steps {
                        dir('Medical_Service') {
                            sh 'java -jar target/Medical_Service-0.0.1-SNAPSHOT.jar &'
                            sleep 20
                        }
                    }
                }
                stage('Deploy Patient Service') {
                    steps {
                        dir('Patient_Service') {
                            sh 'java -jar target/Patient_Service-0.0.1-SNAPSHOT.jar &'
                            sleep 20
                        }
                    }
                }
                stage('Deploy Department Service') {
                    steps {
                        dir('Department_Service') {
                            sh 'java -jar target/Department_Service-0.0.1-SNAPSHOT.jar &'
                            sleep 20
                        }
                    }
                }
            }
        }
        stage('Verify Deployment') {
            steps {
                // Check Eureka Server
                sh "curl -f http://localhost:${SERVICE_REGISTRY_PORT} || exit 1"
                // Check Config Server
                sh "curl -f http://localhost:${CONFIG_SERVER_PORT}/medical-service/default || exit 1"
                // Check API Gateway endpoints
                sh "curl -f http://localhost:${API_GATEWAY_PORT}/medical/doctors || exit 1"
                sh "curl -f http://localhost:${API_GATEWAY_PORT}/patient/medical-records || exit 1"
                sh "curl -f http://localhost:${API_GATEWAY_PORT}/department/departments || exit 1"
            }
        }
    }
    post {
        success {
            echo 'Hospital Management System deployed successfully!'
        }
        failure {
            echo 'Deployment failed. Check logs for details.'
        }
        always {
            // Cleanup (optional): Kill background processes
            sh 'pkill -f "java -jar" || true'
        }
    }
}