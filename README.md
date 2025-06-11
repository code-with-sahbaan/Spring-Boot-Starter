# SPRING BOOT STARTER PROJECT
_BOOSTING PRODUCTIVITY AND DEVELOPMENT WITH BOILERPLATE CODE_

---

## INTRODUCTION
This is a Spring Boot starter project built with Java 17 and Spring Boot 3. It provides starter boilerplate code for building a Spring Boot application with JWT-based authentication and authorization. The project aims to deliver a monolithic application codebase with multi-module feasibility.

## ENVIRONMENT DETAILS
1. JDK 17 (Amazon Corretto).
2. Spring Boot 3+.
3. Dev and Prod profiles for managing properties separately for both environments.

## MODULE DETAILS
The modules used in the starter code give a solid foundation for boosting development. The project comprises 3 Modules and 1 App Service.

1. ### DATA MODULE
    1. The Data Module is designed to keep all database-related functionality under a single, separate umbrella.
    2. It provides:
        - `entities` package for database entities,
        - `repositories` package for JPA repositories,
        - `bootstraps` package for seeding data at application startup,
        - `requestdto` package for handling request DTOs,
        - `responsedto` package for handling response DTOs.

2. ### SECURITY MODULE
    1. The Security Module is designed to encapsulate all authentication and authorization logic in one place.
    2. It provides:
        - `configuration` package for Spring Security configurations (role-based access, custom filter handling, and password encryption),
        - `filters` package for custom authentication and authorization filters,
        - `utils` package for helper methods and utility classes.

3. ### BUSINESS MODULE
    1. The Business Module is responsible for encapsulating all business logic in a separate layer.
    2. It provides:
        - `services` package for service interfaces,
        - `servicesimpl` package for their implementations,
        - Generic service implementation for common operations (e.g., DTO-to-entity and entity-to-DTO mapping).

4. ### APP SERVICE
    1. The App Service is where the application starts running on the machine.
    2. It provides:
        - a Spring Boot `Application` class as the entry point,
        - a `controllers` package for managing REST controllers.

---

**_You are welcome to contribute to this project._**
