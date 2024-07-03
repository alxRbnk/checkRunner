package ru.clevertec.check.service;

import ru.clevertec.check.entity.DiscountCard;

import java.util.Set;

public interface DiscountCardService {
    DiscountCard getDiscountCardByNumber(String number);
    Set<String> getDiscountCardNumbers();
}
