package ru.clevertec.check.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface CustomRound {
    int NUMBER_OF_DECIMAL_PLACES = 2;

    static BigDecimal round(BigDecimal number) {
        return number.setScale(NUMBER_OF_DECIMAL_PLACES, RoundingMode.HALF_UP);
    }
}
