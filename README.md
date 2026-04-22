# SmartLoad Optimization API

## Overview
The SmartLoad Optimization API is a high-performance logistics service designed to match shippers with carriers by finding the optimal combination of orders for a truck [cite: 3, 5]. It maximizes total payout while respecting truck weight and volume capacities [cite: 7, 8].

## Core Features
* **Optimal Load Planning**: Uses a recursive backtracking algorithm to find the most profitable order combination [cite: 17, 86].
* **Logistical Constraints**: Automatically handles route compatibility, weight/volume limits, and hazmat isolation [cite: 79, 102].
* **High Performance**: Engineered to handle up to 22 orders and return results in under 800ms [cite: 80, 103].
* **Stateless Architecture**: Built as a purely in-memory REST API with no database requirements [cite: 14, 94].

## Tech Stack5
* **Language**: Java 17 (Spring Boot) [cite: 93]
* **Build Tool**: Maven [cite: 112]
* **Containerization**: Docker & Docker Compose [cite: 96, 113]

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
    The service will be available at `http://localhost:8080` [cite: 123].

## API Endpoints

### 1. Optimize Load
Find the optimal set of orders for a given truck.

* **URL**: `/api/v1/load-optimizer/optimize` [cite: 15]
* **Method**: `POST` [cite: 130]
* **Headers**: `Content-Type: application/json` [cite: 131]
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

* **URL**: `/actuator/health` [cite: 126]
* **Method**: `GET`

## Evaluation Criteria
The service is optimized for:
1.  **Correctness**: Respects all physical and logistical constraints [cite: 79, 102].
2.  **Performance**: Processes complex combinations ($2^{22}$ states) in under 800ms [cite: 80, 103].
3.  **Code Quality**: Follows SOLID principles and clean code practices [cite: 81, 105].
