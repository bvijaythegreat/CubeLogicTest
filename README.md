# CubeLogicTest
CubeLogicTest
# Marketplace Suspicion Detector

This Java project is designed to detect suspicious trading behavior in a marketplace. It identifies trades and orders that may indicate attempts to manipulate market prices.

## Project Structure

- `Trade.java`: Defines the `Trade` Record with fields for `id`, `price`, `volume`, `side`, and `timestamp`.
- `Order.java`: Defines the `Order` Record with similar fields as `Trade`.
- `Side.java`: Enum to represent `BUY` and `SELL` actions.
- `SuspicionDetector.java`: Interface with a method to find suspicious trades and orders.
- `SuspicionDetectorImpl.java`: Implementation of the `SuspicionDetector` interface.
- `SuspicionDetectorTest.java`: Test cases to validate the detection logic.

## How to Build and Run

### Prerequisites

- Java Development Kit (JDK) 21 or later
- Apache Maven

### Build the Project

1. Clone the repository:
   ```
   git clone https://github.com/bvijaythegreat/CubeLogicTest/
   ```
2. Navigate to the project directory:
   ```
   cd CubeLogicTechTest
   ```
3. Build the project using Maven:
   ```
   mvn clean install
   ```

### Run Tests

Execute the following command to run the test cases:
```bash
mvn test
```

## Usage

The main functionality is implemented in the `SuspicionDetectorImpl` class. You can use this class to detect suspicious trades and orders by passing lists of `Trade` and `Order` objects to the `findSuspicious` method.

