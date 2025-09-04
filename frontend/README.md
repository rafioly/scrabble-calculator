# Frontend

This directory contains the React/TypeScript frontend for the Scrabble Points Calculator.

## Running the Frontend in Development

To run the frontend in a local development environment, follow these steps:

1.  **Navigate to this directory** (`/frontend`) in your terminal.

2.  **Install the dependencies** by running:

    ```sh
    npm install
    ```

3.  **Start the development server** by running:

    ```sh
    npm run dev
    ```

This will start the Vite development server, and the application will be accessible at [http://localhost:5173](http://localhost:5173) by default (check your terminal output for the exact address).

**Note:** For the frontend to be fully functional, the backend service must be running and accessible.

---

## Running Tests

This project uses [Vitest](https://vitest.dev/) and [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/) for component testing.

To run the test suite, navigate to this directory (`/frontend`) and run:

```sh
npm test
```

This will start the Vitest test runner in watch mode, automatically re-running the tests whenever you make changes to a file.
