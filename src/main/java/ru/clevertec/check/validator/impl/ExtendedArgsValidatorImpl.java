package ru.clevertec.check.validator.impl;

import ru.clevertec.check.command.ErrorMessages;
import ru.clevertec.check.command.PrintCommandType;
import ru.clevertec.check.validator.CustomValidator;
import ru.clevertec.check.validator.ValidateRegex;

import static ru.clevertec.check.command.PrintCommandType.BAD_REQUEST;

public enum ExtendedArgsValidatorImpl implements CustomValidator<String[]>, ValidateRegex {
    INSTANCE;

    public void validate(String[] args) {
        String saveToFilePath = "";
        int idQuantity = 0;
        int discountCount = 0;
        int debitCard = 0;
        int pathToFileCount = 0;
        int saveToFileCount = 0;
        for (String line : args) {
            if (ValidateRegex.validateArgs(line, ID_QUANTITY_REGEX)) {
                idQuantity++;
            } else if (ValidateRegex.validateArgs(line, BALANCE_REGEX)) {
                debitCard++;
            } else if (ValidateRegex.validateArgs(line, DISCOUNT_CARD_REGEX)) {
                discountCount++;
            } else if (ValidateRegex.validateArgs(line, PATH_TO_FILE_REGEX)) {
                pathToFileCount++;
            } else if (ValidateRegex.validateArgs(line, SAVE_TO_FILE_REGEX)) {
                saveToFilePath = line.split("=")[1];
                saveToFileCount++;
            }
        }
        if (pathToFileCount != 1 && saveToFileCount == 1) {
            PrintCommandType.define(BAD_REQUEST).execute(saveToFilePath);
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST);
        } else if (saveToFileCount != 1) {
            PrintCommandType.define(BAD_REQUEST).execute();
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST);
        }
        int sumValidArgs = idQuantity + discountCount + debitCard + pathToFileCount + saveToFileCount;
        if (sumValidArgs != args.length || idQuantity < 1 || debitCard != 1) {
            PrintCommandType.define(BAD_REQUEST).execute();
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST);
        }
    }
}