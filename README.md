# Rewards Service

A small Spring Boot application that calculates reward points per customer based on transaction data.

- Java: 17
- Build: Maven
- Default server port: 8080 (configured in `src/main/resources/application.properties`)

Contents
- Project overview
- Build & run
- API endpoints
- Request / Response examples
- Validation rules and error responses
- Tests
- Troubleshooting

---

Project overview

This service accepts transaction data and returns reward point summaries grouped by customer and by month. Reward points calculation (business rules):
- For every dollar spent over $100: 2 points per dollar
- For every dollar spent over $50 up to $100: 1 point per dollar

Example: $120 -> (120 - 100) * 2 + 50 = 40 + 50 = 90 points

Source layout (important files)
- `src/main/java/com/pilla/rewards/RewardsApplication.java` - Spring Boot entry point
- `src/main/java/com/pilla/rewards/controller/RewardsController.java` - REST controller
- `src/main/java/com/pilla/rewards/service/RewardsService.java` - Reward calculation logic
- `src/main/java/com/pilla/rewards/model/Transaction.java` - Transaction DTO (with validation and date format)
- `src/main/java/com/pilla/rewards/model/RewardsSummary.java` - Response DTO
- `src/main/java/com/pilla/rewards/exception/GlobalExceptionHandler.java` - Central exception handler


Build & run

1. Build the project:

    mvn clean package

2. Run from Maven:

    mvn spring-boot:run

   or run the generated jar:

    java -jar target/rewards-0.0.1-SNAPSHOT.jar

The application will bind to port 8080 by default. Change `server.port` in `src/main/resources/application.properties` if needed.


API Endpoints

1) GET /api/rewards
- Description: Returns reward summaries calculated from built-in mock data (`TransactionMockData`).
- Request: GET http://localhost:8080/api/rewards
- Response: 200 OK - list of `RewardsSummary` objects

2) POST /api/rewards/calculate
- Description: Accepts a JSON array of `Transaction` objects and returns rewards summaries grouped by customer.
- Request: POST http://localhost:8080/api/rewards/calculate
- Headers: `Content-Type: application/json`
- Body: JSON array of transactions (see schema below)

Transaction JSON schema (input array element)
- customerId: string (required)
- customerName: string (required)
- amount: number (positive, required)
- transactionDate: string (required) formatted as `yyyy-MM-dd` (e.g. `2026-01-15`)

Example POST body (transactions list):

```json
[
  {
    "customerId": "CUST-005",
    "customerName": "Carol Davis",
    "amount": 115.00,
    "transactionDate": "2026-01-15"
  },
  {
    "customerId": "CUST-005",
    "customerName": "Carol Davis",
    "amount": 170.00,
    "transactionDate": "2026-02-20"
  },
  {
    "customerId": "CUST-005",
    "customerName": "Carol Davis",
    "amount": 225.00,
    "transactionDate": "2026-03-10"
  },
  {
    "customerId": "CUST-005",
    "customerName": "Carol Davis",
    "amount": 235.00,
    "transactionDate": "2026-03-25"
  }
]
```

Expected response (example aggregated `RewardsSummary`) for the above input:

```json
[
  {
    "customerId": "CUST-005",
    "customerName": "Carol Davis",
    "totalPoints": 890,
    "monthlyPoints": {
      "JANUARY": 80,
      "FEBRUARY": 190,
      "MARCH": 620
    }
  }
]
```

Note: The `monthlyPoints` keys are month names in UPPERCASE (e.g. `JANUARY`) because the service groups by `transactionDate.getMonth().toString()`.

Example curl command (useful for Postman "raw" body testing too):

    curl -X POST http://localhost:8080/api/rewards/calculate \
      -H "Content-Type: application/json" \
      -d '@sample-transactions.json'

(Place the JSON example into `sample-transactions.json` and point curl to it.)


Validation rules & error responses

- All `Transaction` fields are validated:
  - `customerId` and `customerName` must be non-blank
  - `amount` must be positive
  - `transactionDate` must be present and parseable as `yyyy-MM-dd`
- If the controller receives an empty array it currently throws an `IllegalArgumentException` with message `Transactions list cannot be empty` which the `GlobalExceptionHandler` maps to a `400 Bad Request` with a JSON body like:

```json
{
  "timestamp": "2026-04-21T12:34:56.789",
  "status": 400,
  "error": "Bad Request",
  "message": "Transactions list cannot be empty"
}
```

- For validation failures (e.g. missing required fields or invalid date format), the server will return a `400 Bad Request` and a descriptive message.


Tests

- Unit / integration tests are in `src/test/java`.
- Run tests with:

    mvn test

Files of interest:
- `src/test/java/com/pilla/rewards/controller/RewardsControllerTest.java` - contains positive and negative MockMvc tests for the `POST /calculate` endpoint.


Troubleshooting

- If requests fail due to JSON date parsing, ensure the `transactionDate` is formatted as `yyyy-MM-dd` (the `Transaction` class uses `@JsonFormat` for this format).
- If you get validation errors, check the server log for the detailed message returned by the `GlobalExceptionHandler`.
- If port 8080 is in use, change `server.port` in `src/main/resources/application.properties`.


Contact @vinaypilla25@gmail.com for questions or issues.

