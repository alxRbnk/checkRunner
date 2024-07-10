package ru.clevertec.check.validator.impl;

import org.junit.jupiter.api.Test;
import ru.clevertec.check.command.ErrorMessages;

import static org.junit.jupiter.api.Assertions.*;

public class DbArgsValidatorImplTest {

    @Test
    public void testValidArguments() {
        String[] validArgs = {
                "1-1",
                "discountCard=1111",
                "balanceDebitCard=100",
                "saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/check",
                "datasource.username=postgres",
                "datasource.password=postgres"
        };
        assertDoesNotThrow(() -> DbArgsValidatorImpl.INSTANCE.validate(validArgs));
    }

    @Test
    public void testMissingSaveToFileArgument() {
        String[] argsWithoutSaveToFile = {
                "discountCard=1111",
                "balanceDebitCard=100",
                "datasource.url=jdbc:postgresql://localhost:5432/check",
                "datasource.username=postgres",
                "datasource.password=postgres"
        };
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DbArgsValidatorImpl.INSTANCE.validate(argsWithoutSaveToFile);
        });
        assertEquals(ErrorMessages.BAD_REQUEST, exception.getMessage());
    }

    @Test
    public void testInvalidArguments() {
        String[] invalidArgs = {
                "discountCard=1111",
                "balanceDebitCard=100",
                "saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/check",
                "datasource.password=postgres"
        };

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DbArgsValidatorImpl.INSTANCE.validate(invalidArgs);
        });
        assertEquals(ErrorMessages.BAD_REQUEST, exception.getMessage());
    }

    @Test
    public void testInvalidBalanceDebitCardFormat() {
        String[] invalidBalanceDebitCard = {
                "discountCard=1111",
                "balanceDebitCard=100.123",
                "saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/check",
                "datasource.username=postgres",
                "datasource.password=postgres"
        };

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DbArgsValidatorImpl.INSTANCE.validate(invalidBalanceDebitCard);
        });
        assertEquals(ErrorMessages.BAD_REQUEST, exception.getMessage());
    }

    @Test
    public void testInvalidDiscountCardFormat() {
        String[] invalidDiscountCard = {
                "discountCard=111",
                "balanceDebitCard=100",
                "saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/check",
                "datasource.username=postgres",
                "datasource.password=postgres"
        };
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DbArgsValidatorImpl.INSTANCE.validate(invalidDiscountCard);
        });
        assertEquals(ErrorMessages.BAD_REQUEST, exception.getMessage());
    }
}
