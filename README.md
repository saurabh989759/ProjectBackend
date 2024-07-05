
### Backend Repository `README.md`

```markdown
# Project Management System Backend

This repository contains the backend component of the Project Management System. The backend is built using Spring Boot and is responsible for managing the business logic, data storage, and API endpoints for the application.

## Description

The backend provides a RESTful API for the frontend application, handling tasks such as user authentication, project and task management, and payment processing. It integrates with a MySQL database for persistent data storage and uses JWT for secure authentication.

## Features

- **User Authentication**: Secure login and registration using JWT.
- **Project and Task Management**: CRUD operations for projects and tasks.
- **Payment Processing**: Integrates with Razorpay for seamless payment transactions.
- **API Documentation**: Swagger for API documentation and testing.

## Technologies Used

- Spring Boot
- Spring Data JPA
- MySQL
- Razorpay
- Swagger

## Getting Started

### Prerequisites

- Java 11+
- Maven 3.6+
- MySQL (local or cloud instance)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/saurabh989759/ProjectBackend.git
   cd ProjectBackend
