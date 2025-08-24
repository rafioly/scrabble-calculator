# --- Stage 1: Build the React Application ---
# Use an official Node.js image as the builder environment
FROM node:18-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json to leverage Docker cache
COPY package*.json ./

# Install project dependencies
RUN npm install

# Copy the rest of the application source code
COPY . .

# Build the application for production
RUN npm run build

# --- Stage 2: Serve the Application with Nginx ---
# Use a lightweight Nginx image for the production environment
FROM nginx:stable-alpine

# Copy the build output (the static files) from the builder stage
# to the default directory Nginx serves files from
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy the custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80 to the outside world
EXPOSE 80

# The default Nginx command will start the server
