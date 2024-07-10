# Check Calculation Project

This project calculates the total price of purchased products, applies discounts using discount cards, and manages debit card balances. The calculation results are outputted to a CSV file and logged in the console.

## Prerequisites

- Java 11 or higher
- Gradle 8.5 or higher

## Building the Project

To compile the project, use the following Gradle command:
```bash
gradle build
```

## Running the Project

After compiling, you can run the project using the `java` command. Here is an example of how to run the `CheckRunner` class:
```bash
java -jar build/libs/first-1.0-SNAPSHOT.jar 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100 saveToFile=./result.csv datasource.url=jdbc:postgresql://localhost:5432/check datasource.username=postgres datasource.password=postgres
```

### Arguments

The arguments to the program should be in the following format:

- `<productId>-<quantity>`: Specifies the product ID and the quantity to be purchased. For example, `3-1` means product with ID 3 and quantity 1.
- `discountCard=<cardNumber>`: Specifies the discount card number to be used. For example, `discountCard=1111`.
- `balanceDebitCard=<balance>`: Specifies the debit card balance. For example, `balanceDebitCard=100`.
- `saveToFile=<filePath>`: Specifies the file path to save the result. For example, `saveToFile=./result.csv`.
- `datasource.url=<dbUrl>`: Specifies the database URL. For example, `datasource.url=jdbc:postgresql://localhost:5432/check`.
- `datasource.username=<dbUsername>`: Specifies the database username. For example, `datasource.username=postgres`.
- `datasource.password=<dbPassword>`: Specifies the database password. For example, `datasource.password=postgres`.

### Example

The command below calculates the total price for 1 unit of product with ID 3, 5 units of product with ID 2, and 1 unit of product with ID 5, applies a discount card with the number 1111, uses a debit card with a balance of 100, and saves the result to `./result.csv`:
```bash
java -jar build/libs/first-1.0-SNAPSHOT.jar 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100 saveToFile=./result.csv datasource.url=jdbc:postgresql://localhost:5432/check datasource.username=postgres datasource.password=postgres
```

## Output

The result of the check calculation will be written to a CSV file located at the specified path and also logged to the console.

## Project Structure
- `ru.clevertec.check.CheckRunner: The main class to run the project.
- `ru.clevertec.check.factory.ServiceFactory: Factory interfaces and implementations for creating services.
- `ru.clevertec.check.service: Service interfaces and implementations for handling business logic.
- `ru.clevertec.check.dao: Data Access Object (DAO) interfaces and implementations for database interactions.
- `ru.clevertec.check.validator: Custom validators for input arguments.
- `ru.clevertec.check.util: Utility classes for reading CSV files and rounding values.
- `ru.clevertec.check.command: Command pattern implementation for handling errors.
- `ru.clevertec.check.entity: Entity classes representing products, items, and discount cards.

## Sample Command for Running with Gradle

```bash
java -jar build/libs/check-calculation-1.0-SNAPSHOT.jar 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100 saveToFile=./result.csv datasource.url=jdbc:postgresql://localhost:5432/check datasource.username=postgres datasource.password=postgres
```

This command will process the specified products, apply the discount card, check the debit card balance, and save the result to the specified CSV file while also logging the result to the console.
