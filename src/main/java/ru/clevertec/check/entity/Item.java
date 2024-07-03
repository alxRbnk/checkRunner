package ru.clevertec.check.entity;

import lombok.*;
import ru.clevertec.check.util.CustomRound;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Item {
    private Product product;
    private int quantity;

    public BigDecimal getTotalPrice() {
        return CustomRound.round(product.getPrice()
                .multiply(BigDecimal.valueOf(quantity)));
    }
}
