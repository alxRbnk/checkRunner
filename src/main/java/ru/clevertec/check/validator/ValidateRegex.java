package ru.clevertec.check.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ValidateRegex {
    String ID_QUANTITY_REGEX = "\\d+-\\d+";
    String DISCOUNT_CARD_REGEX = "discountCard=[0-9]{4}$";
    String BALANCE_REGEX = "balanceDebitCard=-?[0-9]+(\\.[0-9]{1,2})?$";
    String PATH_TO_FILE_REGEX = "pathToFile=.+\\.csv$";
    String SAVE_TO_FILE_REGEX = "saveToFile=.+\\.csv$";

    static boolean validateArgs(String line, String regex) {
        boolean flag = false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            flag = true;
        }
        return flag;
    }
}
