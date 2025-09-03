# Backend

This directory contains the Java/Spring Boot backend for the Scrabble Points Calculator.

## Running the Backend in Development

To run the backend on your local machine for development, it is recommended to use the `local` Spring profile. This allows you to easily manage database connections without modifying the main configuration files.

1.  **Configure your local database connection:**
    Open the `src/main/resources/application-local.yml` file. Verify that the `spring.datasource.url` property and other credentials match your local PostgreSQL setup.

2.  **Run the application with the `local` profile:**
    Navigate to this directory (`/backend`) in your terminal and run the following command:

    ```sh
    ./gradlew bootRun --args='--spring.profiles.active=local'
    ```

This command starts the Spring Boot application and tells it to apply the settings from `application-local.yml`. The API will be accessible at [http://localhost:8080](http://localhost:8080) by default.

### Prerequisites

- **Java 21** must be installed on your machine.
- **A PostgreSQL database** must be running and accessible to the application as configured in `application-local.yml`.

---

## Running Tests

This project has both unit and integration tests. You can run them using the following Gradle commands:

- **Run only unit tests:**
  ```sh
  ./gradlew test
  ```

- **Run only integration tests:**
  ```sh
  ./gradlew integrationTest
  ```

- **Run all tests (unit and integration):**
  ```sh
  ./gradlew check
  ```

Test reports can be found in the `build/reports/tests/` directory after the tests have been executed.

---

## API Documentation

This project uses `springdoc-openapi` to generate interactive API documentation with Swagger UI.

Once the application is running, you can access and interact with the API at:
**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

The UI is the primary source of truth for all API endpoints, request/response schemas, and allows you to try executing GET API calls directly from your browser.
