\# Meridian CRM



\*\*A Multi-Tenant SaaS Customer Relationship Management Platform\*\*



Meridian CRM is a full-stack SaaS CRM application designed to help organizations manage customers, users, sales activities, business relationships, and support operations through a centralized platform.



The project follows a \*\*Modular Monolith Architecture\*\*, with an emphasis on clean code, scalability, security, maintainability, and real-world software engineering practices.



\## Project Status



🚧 Under Active Development



The backend foundation is currently under development. Frontend development and additional CRM modules are planned.



\## Technology Stack



| Layer | Technologies |

|---|---|

| Frontend | Angular, TypeScript |

| UI | Angular Material, Tailwind CSS |

| Backend | Java 21, Spring Boot |

| ORM | Spring Data JPA, Hibernate |

| Database | MySQL 8 |

| Database Migrations | Flyway |

| Security | Spring Security |

| Build Tool | Maven |

| API Documentation | OpenAPI / Swagger (planned) |

| Testing | JUnit, Mockito (planned) |

| Version Control | Git, GitHub |



\## Architecture



Meridian CRM follows a Modular Monolith architecture.



The application is organized into independent business modules within a single deployable backend application.



Planned high-level modules:



\- Organization Management

\- User Management

\- Role and Permission Management

\- Lead Management

\- Contact Management

\- Account Management

\- Deal and Pipeline Management

\- Activity and Task Management

\- Helpdesk and Ticket Management

\- Workflow Automation



\## Implemented Features



\### Backend Foundation



\- Spring Boot application setup

\- MySQL database integration

\- Flyway database migrations

\- JPA entity mapping

\- Global exception handling

\- Standardized API error responses

\- Request validation infrastructure



\### Organization Management



\- Organization entity and repository

\- Organization REST APIs

\- Organization service layer

\- Organization status management

\- Organization search specifications



\### User Management



\- User entity and repository

\- User REST APIs

\- User service layer

\- User update and status management

\- Pagination and search infrastructure

\- Organization-scoped email uniqueness



\### Role and Permission Foundation



\- Role entity

\- Permission entity

\- Initial role and permission modeling

\- Organization-aware role database migration



\### Security Foundation



\- Spring Security configuration

\- Password encoding configuration



\## Planned Engineering Capabilities



The project roadmap includes:



\- JWT authentication and refresh tokens

\- Hybrid Role-Based Access Control (RBAC)

\- Multi-tenant data isolation

\- Audit logging

\- Advanced search and filtering

\- Redis caching

\- RabbitMQ asynchronous processing

\- Retry mechanisms and Dead Letter Queues

\- Transactional Outbox Pattern

\- Kafka and event-driven architecture fundamentals

\- Docker-based deployment

\- CI/CD pipelines

\- Application monitoring and observability



These capabilities will be implemented progressively as the project evolves.



\## Project Structure



```text

MERIDIAN-CRM/

│

├── backend/

│   ├── src/

│   │   ├── main/

│   │   │   ├── java/com/meridian/backend/

│   │   │   │   ├── common/

│   │   │   │   ├── organization/

│   │   │   │   ├── permission/

│   │   │   │   ├── role/

│   │   │   │   ├── security/

│   │   │   │   └── user/

│   │   │   │

│   │   │   └── resources/

│   │   │       ├── db/migration/

│   │   │       └── application.properties

│   │   │

│   │   └── test/

│   │

│   └── pom.xml

│

├── frontend/

│

├── .github/

│

├── .gitignore

└── README.md

```



\## Getting Started



\### Prerequisites



\- Java 21

\- MySQL 8

\- Git



\### 1. Clone the repository



```bash

git clone https://github.com/Dhanarajan-S/meridian-crm.git

```



\### 2. Navigate to the backend



```bash

cd meridian-crm/backend

```



\### 3. Configure the database



Create a MySQL database:



```sql

CREATE DATABASE meridian\_crm;

```



Configure your database credentials using environment variables.



Windows CMD:



```cmd

set DB\_PASSWORD=your\_mysql\_password

```



The backend uses the `DB\_PASSWORD` environment variable for database authentication.



\### 4. Run the application



Windows:



```cmd

mvnw.cmd spring-boot:run

```



The backend runs at:



```text

http://localhost:8080

```



Flyway manages database schema migrations during application startup.



\## Development Philosophy



Meridian CRM is being developed with a focus on practical software engineering principles:



\- Clean Architecture and separation of concerns

\- Modular and maintainable code

\- REST API design principles

\- Database normalization and indexing

\- Secure authentication and authorization

\- Multi-tenant application design

\- Exception handling and validation

\- Testing and code quality

\- Performance and scalability

\- Production-oriented engineering practices



\## Roadmap



\- \[x] Backend project initialization

\- \[x] MySQL integration

\- \[x] Flyway migration setup

\- \[x] Organization management foundation

\- \[x] User management foundation

\- \[x] Role and permission entities

\- \[x] Initial security configuration

\- \[ ] Complete backend security and RBAC

\- \[ ] Multi-tenant data isolation

\- \[ ] Audit logging

\- \[ ] CRM core modules

\- \[ ] Helpdesk module

\- \[ ] Workflow automation

\- \[ ] Angular frontend

\- \[ ] Automated testing

\- \[ ] Docker and CI/CD

\- \[ ] Deployment and monitoring



\## License



This project is currently under development. Licensing will be determined before public distribution.

