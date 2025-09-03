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
- **A PostgreSQL database** must be running and accessible to the application. You will need to configure the database connection details in `src/main/resources/application.properties` to match your local setup.

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

The backend exposes a RESTful API for calculating scores and managing the leaderboard.

### Scoring Rules

- **`GET /api/scoring-rules`**
  - **Description:** Retrieves the mapping of letters to their Scrabble point values.
  - **Success Response (200 OK):**
    ```json
    {
      "success": true,
      "message": "Data retrieved successfully.",
      "data": {
        "A": 1, "B": 3, "C": 3, ...
      }
    }
    ```

### Leaderboard

- **`GET /api/leaderboards`**
  - **Description:** Retrieves the top scores from the leaderboard.
  - **Query Parameters:**
    - `top` (optional, default: 10): The number of top scores to return.
  - **Success Response (200 OK):**
    ```json
    {
      "success": true,
      "message": "Data retrieved successfully.",
      "data": [
        { "rank": 1, "word": "HELLO", "score": 8 },
        { "rank": 2, "word": "WORLD", "score": 7 }
      ]
    }
    ```

- **`POST /api/leaderboards`**
  - **Description:** Submits a new word to be scored and saved to the leaderboard.
  - **Request Body:**
    ```json
    { "word": "EXAMPLE" }
    ```
  - **Success Response (200 OK):**
    ```json
    {
      "success": true,
      "message": "New score saved successfully.",
      "data": { "word": "EXAMPLE", "score": 18 }
    }
    ```

- **`DELETE /api/leaderboards`**
  - **Description:** Deletes all scores from the leaderboard.
  - **Success Response (204 No Content):** An empty response indicating success.
