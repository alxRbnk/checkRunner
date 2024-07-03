package ru.clevertec.check.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Product {
    private int id;
    private String description;
    private BigDecimal price;
    private int quantityInStock;
    private boolean isWholesale;
}
