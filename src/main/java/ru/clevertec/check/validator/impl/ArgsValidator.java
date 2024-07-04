package ru.clevertec.check.validator.impl;

import ru.clevertec.check.command.ErrorMessages;
import ru.clevertec.check.command.PrintCommandType;
import ru.clevertec.check.command.PrintErrorCommand;
import ru.clevertec.check.validator.CustomValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.clevertec.check.command.PrintCommandType.BAD_REQUEST;

public enum ArgsValidator implements CustomValidator<String[]> {
    INSTANCE;
    private static final String ID_QUANTITY_REGEX = "\\d+-\\d+";
    private static final String DISCOUNT_CARD_REGEX = "discountCard=[0-9]{4}$";
    private static final String BALANCE_REGEX = "balanceDebitCard=-?[0-9]+(\\.[0-9]{1,2})?$";

    public void validate(String[] args) {
        int idQuantity = 0;
        int discountCount = 0;
        int debitCard = 0;
        for (String line : args) {
            if (validateArgs(line, ID_QUANTITY_REGEX)) {
                idQuantity++;
            } else if (validateArgs(line, BALANCE_REGEX)) {
                debitCard++;
            } else if (validateArgs(line, DISCOUNT_CARD_REGEX)) {
                discountCount++;
            }
        }
        int sumValidArgs = idQuantity + discountCount + debitCard;
        if (sumValidArgs != args.length || idQuantity < 1 || debitCard != 1) {
            PrintCommandType.define(BAD_REQUEST).execute();
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST);
        }
    }

    private boolean validateArgs(String line, String regex) {
        boolean flag = false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            flag = true;
        }
        return flag;
    }
}
