package ru.clevertec.check.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.clevertec.check.util.CustomRound;

import java.math.BigDecimal;

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
