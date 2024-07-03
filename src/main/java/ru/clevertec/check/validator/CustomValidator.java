package ru.clevertec.check.validator;

import ru.clevertec.check.command.PrintCommand;
import ru.clevertec.check.command.PrintCommandType;

import java.util.Arrays;

public class CustomValidator {

    public void validateArgs(String[] args){
        if (args.length < 3) { //fixme
            System.out.println(Arrays.toString(args));
            PrintCommandType.define(PrintCommandType.BAD_REQUEST).execute();
            throw new IllegalArgumentException("It is necessary to pass the parameters:" +
                    " id-quantity discountCard=xxxx balanceDebitCard=xxxx");
        }
    }
}
