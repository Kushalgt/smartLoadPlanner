# SmartLoad Optimization API

## Overview
The SmartLoad Optimization API is a high-performance logistics service designed to match shippers with carriers by finding the optimal combination of orders for a truck [cite: 3, 5]. It maximizes total payout while respecting truck weight and volume capacities.

## Core Features
* **Optimal Load Planning**: Uses a recursive backtracking algorithm to find the most profitable order combination.
* **Logistical Constraints**: Automatically handles route compatibility, weight/volume limits, and hazmat isolation.
* **High Performance**: Engineered to handle up to 22 orders and return results in under 800ms.
* **Stateless Architecture**: Built as a purely in-memory REST API with no database requirements.

## Tech Stack5
* **Language**: Java 17 (Spring Boot)
* **Build Tool**: Maven
* **Containerization**: Docker & Docker Compose

## How to Run

### Prerequisites
* Docker and Docker Compose installed.

### Steps
1.  **Clone the Repository**
    ```bash
    git clone https://github.com/Kushalgt/smartLoadPlanner.git
    cd smartLoadPlanner
    ```

2.  **Build and Start the Service**
    ```bash
    docker compose up --build
    ```
    The service will be available at `http://localhost:8080`.

## API Endpoints

### 1. Optimize Load
Find the optimal set of orders for a given truck.

* **URL**: `/api/v1/load-optimizer/optimize`
* **Method**: `POST`
* **Headers**: `Content-Type: application/json`
* **Sample Request**:
    ```json
    {
      "truck": {
        "id": "truck-123",
        "maxWeightLbs": 44000,
        "maxVolumeCuft": 3000
      },
      "orders": [
        {
          "id": "ord-001",
          "payoutCents": 250000,
          "weightLbs": 18000,
          "volumeCuft": 1200,
          "origin": "Los Angeles, CA",
          "destination": "Dallas, TX",
          "isHazmat": false
        }
      ]
    }
    ```

### 2. Health Check
Monitor the service status.

* **URL**: `/actuator/health`
* **Method**: `GET`

## Evaluation Criteria
The service is optimized for:
1.  **Correctness**: Respects all physical and logistical constraints.
2.  **Performance**: Processes complex combinations ($2^{22}$ states) in under 800ms.
3.  **Code Quality**: Follows SOLID principles and clean code practices.
