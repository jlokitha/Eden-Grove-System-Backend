<h1 align="center">Eden Grove Backend</h1>

<p align="center">
Eden Grove is a comprehensive system designed to manage the operations of Green Shadow, specializing in root crops and cereals. The system focuses on managing fields, crops, staff, monitoring logs, vehicles, and equipment to support the company's transition to large-scale production.
</p>

## Project Overview

Green Shadow operates at both national and international levels and is renowned for high-quality production. The Eden Grove system aims to systemize the following areas:

- **Field Management**: Manages land allocated for cultivation.
- **Crop Management**: Handles information related to crop types and growth stages.
- **Staff Management**: Manages human resources and their assignments.
- **Monitoring Log**: Records observations and activities related to fields and crops.
- **Vehicle Management**: Oversees vehicle management and allocations for staff and operations.
- **Equipment Management**: Manages agricultural equipment used in various operations.

## Technologies Used

This project was developed using a range of modern backend technologies and frameworks to ensure robust performance, scalability, and secure data handling.

[![SPRING](https://img.shields.io/badge/Spring_Framework-black?style=for-the-badge&logo=spring&logoColor=green)](https://spring.io/projects/spring-framework)

[![SPRING DATA JPA](https://img.shields.io/badge/Spring_Data_JPA-black?style=for-the-badge&logo=spring&logoColor=green)](https://spring.io/projects/spring-data-jpa)

[![HIBERNATE](https://img.shields.io/badge/Hibernate-black?style=for-the-badge&logo=Hibernate&logoColor=BBAE79)](https://hibernate.org/orm/)

[![MAVEN](https://img.shields.io/badge/Maven-black?style=for-the-badge&logo=apachemaven&logoColor=C77361)](https://maven.apache.org/download.cgi)

[![MySQL](https://img.shields.io/badge/Mysql-black?style=for-the-badge&logo=mysql&logoColor=08668E")](https://www.mysql.com/downloads/)

[![POSTMAN](https://img.shields.io/badge/Postman-black?style=for-the-badge&logo=Postman&logoColor=FF713D")](https://www.postman.com/downloads/)

## Features

- **User Access**: Users can log into the system as MANAGER, ADMINISTRATIVE, or SCIENTIST.
- **CRUD Operations**: Principal users can perform CRUD operations on relevant entities.
- **Permissions and Access Limitations**:
    - **MANAGER**: Full access to perform all CRUD operations.
    - **ADMINISTRATIVE**: Cannot edit crop data, field data, or monitor logs related to crop details.
    - **SCIENTIST**: Cannot modify staff, vehicle, or equipment data.

<!-- ## Technologies Used

- **Java**: Backend programming language.
- **Spring Boot**: Framework for building the backend application.
- **Hibernate**: ORM framework for database interactions.
- **PostgreSQL**: Database for storing application data.
- **JWT**: For user authentication and access control.
- **Lombok**: To reduce boilerplate code. -->

## Getting Started

Follow these steps to set up the project locally and get it running on your machine. The instructions will guide you through the process of cloning the repository, installing dependencies, and configuring any required settings.

### Prerequisites

Before you begin, ensure you have the following installed on your system:

- **[Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)**: Ensure you have JDK installed and configured on your system to compile and run application.
- **[MySQL](https://dev.mysql.com/downloads/)**: Ensure you have MySQL installed and running locally or remotely.
- **[Maven](https://mvnrepository.com/)**: Used for dependency management and building the project.
- **[Postman](https://www.postman.com/downloads/)** or any API client to test the API (optional, but recommended).

### Installation

To install and run the System, follow these steps:

1. Clone the repository:

   ```sh
   git clone https://github.com/jlokitha/Eden-Grove-System-Backend.git
   ```

2. Navigate to the project directory:

   ```sh
   cd Eden-Grove-System-Backend
   ```

3. Open the project in IntelliJ IDEA:

   ```bash
   idea .
   ```

## API Documentation

Refer to the [Postman API Documentation](https://documenter.getpostman.com/view/35384124/2sAYBaBAHr) for detailed API endpoints and usage instructions.

## Frontend Repository

Access the frontend repository on GitHub [here](https://github.com/jlokitha/Eden-Grove-System-Frontend.git).

## License

Distributed under the MIT License. See [LICENSE](LICENSE) for more information.

##

<div align="center">
<a href="https://github.com/jlokitha" target="_blank"><img src = "https://img.shields.io/badge/GitHub-000000?style=for-the-badge&logo=github&logoColor=white"></a>
<a href="https://git-scm.com/" target="_blank"><img src = "https://img.shields.io/badge/Git-000000?style=for-the-badge&logo=git&logoColor=white"></a>
<a href="https://spring.io/projects/spring-framework" target="_blank"><img src = "https://img.shields.io/badge/Spring_Framework-000000?style=for-the-badge&logo=spring&logoColor=white"></a>
<a href="https://spring.io/projects/spring-data-jpa" target="_blank"><img src = "https://img.shields.io/badge/Spring_Data_JPA-000000?style=for-the-badge&logo=spring&logoColor=white"></a>
<a href="https://www.mysql.com/downloads/" target="_blank"><img src = "https://img.shields.io/badge/MySQL-000000?style=for-the-badge&logo=mysql&logoColor=white"></a>
<a href="https://maven.apache.org/download.cgi" target="_blank"><img src = "https://img.shields.io/badge/Maven-000000?style=for-the-badge&logo=apachemaven&logoColor=white"></a>
<a href="https://www.postman.com/downloads/" target="_blank"><img src = "https://img.shields.io/badge/Postman-000000?style=for-the-badge&logo=Postman&logoColor=white"></a>
<a href="https://www.jetbrains.com/idea/download/?section=windows" target="_blank"><img src = "https://img.shields.io/badge/intellij-000000?style=for-the-badge&logo=intellijidea&logoColor=white"></a>
</div> <br>

<p align="center">
  &copy; 2024 Janindu Lokitha
</p>
