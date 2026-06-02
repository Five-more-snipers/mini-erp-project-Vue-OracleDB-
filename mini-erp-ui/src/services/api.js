// api.js - Axios HTTP Client Configuration
// Purpose: Centralized API service for making HTTP requests to the Spring Boot backend.
// Why? Separation of concerns - all API calls are in one place, making it easier to maintain.
// In an interview, you'd explain: "I use a service layer to decouple API logic from UI components."

import axios from 'axios'

// Create an axios instance with a base URL pointing to the Spring Boot backend.
// baseURL: All requests will be prefixed with this URL.
// Example: api.get('/api/items') becomes GET http://localhost:8080/api/items
const api = axios.create({
  baseURL: 'http://localhost:8080'
})

export default api
