# Backend

This directory contains the Java/Spring Boot backend for the Scrabble Points Calculator.

## Running the Backend in Development

To run the backend in a local development environment, follow these steps:

1.  **Navigate to this directory** (`/backend`) in your terminal.

2.  **Run the application** using the Gradle wrapper:

    ```sh
    ./gradlew bootRun
    ```

This will start the Spring Boot application, and the API will be accessible at [http://localhost:8080](http://localhost:8080) by default.

### Prerequisites

- **Java 21** must be installed on your machine.
- **A PostgreSQL database** must be running and accessible to the application. You will need to configure the database connection details in `src/main/resources/application.yml` to match your local setup.
