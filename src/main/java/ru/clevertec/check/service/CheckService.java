package ru.clevertec.check.service;

import ru.clevertec.check.entity.Item;

import java.math.BigDecimal;
import java.util.List;

public interface CheckService {
    void addItem(int productId, int quantity);
    void applyDiscountCard(String discountCardNumber);
    void setBalanceDebitCard(BigDecimal balanceDebitCard);
    void calculateTotalSum();
    List<Item> getItems();
    BigDecimal getTotalSum();
    BigDecimal getTotalDiscount();
    BigDecimal getBalanceDebitCard();
}
