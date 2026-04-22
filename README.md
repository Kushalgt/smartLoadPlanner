# SmartLoad Optimization API

## Overview
The SmartLoad Optimization API is a high-performance logistics service designed to match shippers with carriers by finding the optimal combination of orders for a truck [cite: 3, 5]. It maximizes total payout while respecting truck weight and volume capacities.

## Core Features
* **Optimal Load Planning**: Uses a recursive backtracking algorithm to find the most profitable order combination.
* **Logistical Constraints**: Automatically handles route compatibility, weight/volume limits, and hazmat isolation.
* **High Performance**: Engineered to handle up to 22 orders and return results in under 800ms.
* **Stateless Architecture**: Built as a purely in-memory REST API with no database requirements.

## Tech Stack
* **Language**: Java 17 (Spring Boot)
* **Build Tool**: Maven
* **Containerization**: Docker & Docker Compose
## Technical Optimizations

* **Fast Pruning via Compatibility Keys:** To achieve sub-800ms performance, the backtracking algorithm avoids redundant, multi-field object comparisons. Instead, it generates a composite `compatibilityCriteria` key (`Origin + Destination + isHazmat`) for early evaluation. 
    * *Example:* `"Los Angeles, CADallas, TXtrue"`
    * This allows the recursion tree to instantly prune fundamentally incompatible branches using a single $O(1)$ string match, drastically reducing the state space.
## System Design & Trade-offs

### Why No Memoization (Caching)?
While Dynamic Programming (memoization) is often paired with recursion, it was intentionally omitted from this architecture to meet the strict <800ms latency requirement. 

1. **Granular State Space:** The cache key would require tracking the current index, exact weight, exact volume, and date boundaries. Because weight and volume are highly granular (e.g., a truck can have 44,925 lbs vs 44,926 lbs), the probability of hitting the exact same state via a different branch is near zero.
2. **Garbage Collection (GC) Overhead:** At $n=22$, the algorithm evaluates millions of potential paths. Creating a `CacheKey` object for every state would overwhelm the JVM heap, triggering aggressive Garbage Collection pauses that would severely degrade performance.
3. **Primitive Speed vs. HashMap Overhead:** Raw recursive method calls using primitive math operations (addition/subtraction) execute in nanoseconds. The overhead of computing hashes and querying a `HashMap` at every node is significantly slower than aggressively pruning the tree and moving forward.

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
