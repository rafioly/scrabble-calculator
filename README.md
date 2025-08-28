# Scrabble Points Calculator

This is a full-stack web application that calculates the point value of a word based on Scrabble scoring rules. It features a real-time scoring interface and a leaderboard to track top submissions.

This project is structured as a monorepo, containing a Java/Spring Boot backend and a React/TypeScript frontend.

---

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3**
- **PostgreSQL**: Database for the leaderboard.
- **Gradle**: Build tool.
- **Docker**: Containerization.

### Frontend
- **React**
- **TypeScript**
- **Vite**: Frontend tooling and development server.
- **Material-UI (MUI)**: UI component library.
- **Nginx**: Serves the production build of the frontend.
- **Docker**: Containerization.

---

## Project Structure

The project is a monorepo with the following structure:

```
/scrabble-calculator
├── backend/         # The Spring Boot backend application
├── frontend/        # The React frontend application
├── docker-compose.yml # Orchestrates the entire application stack
└── README.md        # This file
```

--- 

## Running the Application

The entire application stack (database, backend, and frontend) is containerized and managed by Docker Compose.

### Prerequisites

- [Docker](https://www.docker.com/get-started) must be installed and running on your machine.

### Instructions

1.  **Clone the repository** to your local machine.

2.  **Navigate to the project root directory** in your terminal.

3.  **Run the following command** to build the images and start all services:

    ```sh
    docker-compose up --build
    ```

    This command will:
    - Pull the PostgreSQL image.
    - Build the Docker image for the backend service.
    - Build the Docker image for the frontend service.
    - Start the database, backend, and frontend containers.

4.  **Wait for all services to start.** The backend will wait for the database to be healthy before it starts. The frontend will wait for the backend.

--- 

## Accessing the Application

Once all services are running, the application will be accessible at the following addresses:

- **Frontend Application**: [http://localhost:3000](http://localhost:3000)
- **Backend API**: [http://localhost:8080](http://localhost:8080)

API requests from the frontend are automatically proxied to the backend by the Nginx server in the frontend container.
