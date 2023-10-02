# Vending Machine API

This project simulates a vending machine system, providing a RESTful API that allows interaction with product listings and user accounts.

## Prerequisites

- **JDK 17**: The project requires JDK 17 to run.

## Project Overview

Designed with Spring Boot, this Vending Machine API has capabilities split between user roles:

- **Seller**:
  - Add, update, or remove product listings.
  
- **Buyer**:
  - Deposit specific coin denominations 0.05, 0.10, 0.20, 0.50, 1.00.
  - Purchase products and get change.
  - Reset their deposit.

Swagger is integrated for convenient API exploration.

## Setup and Run

1. Clone the repository.
2. Open the project in your preferred IDE. While I've tested and developed this using IntelliJ IDEA, it's compatible with other IDEs like Eclipse.
3. Run the main application class to start the server.

## Accessing the API

Visit the Swagger UI documentation for a detailed overview of available endpoints: [Swagger UI](http://localhost:8080/swagger-ui/index.html#/)

## Sample Users

For testing purposes, the application is initialized with sample users:

- **Seller**: 
  - Username: `seller`
  - Password: `s123`
  
- **Buyer**:
  - Username: `buyer`
  - Password: `b123`
  
- **Admin**:
  - Username: `admin`
  - Password: `admin`

These are set up in the data initializer for ease of testing. Feel free to create and test with new users if needed.

## Security Aspects

Basic authentication is implemented. Role-based access control is in place, ensuring appropriate access rights for different functionalities. Detailed configuration is available in the `SecurityConfig` class.

## Feedback

After reviewing the project, I welcome any feedback or queries. Please send them to my email or through the GitHub repository's issue tracker.

Good luck with the evaluation!