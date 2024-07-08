# Check Calculation Project

This project calculates the total price of purchased products, applies discounts using discount cards, and manages debit card balances. The calculation results are outputted to a CSV file and logged in the console.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Building the Project

To compile the project, use the following Maven command:
```bash
mvn clean compile
```
## Running the Project

After compiling, you can run the project using the `java` command. Here is an example of how to run the `CheckRunner` class:
```bash
java -cp target/classes ./src/main/java/ru/clevertec/check/CheckRunner.java 1-1 2-2 discountCard=1111 balanceDebitCard=300 pathToFile=./products.csv saveToFile=./result.csv
```

### Arguments

The arguments to the program should be in the following format:

- `<productId>-<quantity>`: Specifies the product ID and the quantity to be purchased. For example, `3-1` means product with ID 3 and quantity 1.
- `discountCard=<cardNumber>`: Specifies the discount card number to be used. For example, `discountCard=1111`.
- `balanceDebitCard=<balance>`: Specifies the debit card balance. For example, `balanceDebitCard=100`.
- `pathToFile=<filePath>`: Specifies the relative path to the CSV file containing product information. For example, `pathToFile=./src/main/resources/products.csv`.
- `saveToFile=<filePath>`: Specifies the relative path to save the output CSV file. For example, `saveToFile=./result.csv`.

### Example

The command below calculates the total price for 1 unit of product with ID 1, 2 units of product with ID 2, applies a discount card with the number 1111, and uses a debit card with a balance of 300:
```bash
java -cp target/classes ./src/main/java/ru/clevertec/check/CheckRunner.java 1-1 2-2 discountCard=1111 balanceDebitCard=300 pathToFile=./products.csv saveToFile=./result.csv
```

## Output

The result of the check calculation will be written to a CSV file located at `./result.csv` and also logged to the console.

## File Structure

- `src/main/resources/products.csv`: Contains product information.
- `src/main/resources/discountCards.csv`: Contains discount card information.
- `./result.csv`: The output file containing the result of the check calculation.

## Project Structure

- `ru.clevertec.check.CheckRunner`: The main class to run the project.
- `ru.clevertec.check.factory.ServiceFactory`: Factory interfaces and implementations for creating services.
- `ru.clevertec.check.service`: Service interfaces and implementations for handling business logic.
- `ru.clevertec.check.validator`: Custom validators for input arguments.
- `ru.clevertec.check.util`: Utility classes for reading CSV files and rounding values.
- `ru.clevertec.check.command`: Command pattern implementation for handling errors.
- `ru.clevertec.check.entity`: Entity classes representing products, items, and discount cards.
