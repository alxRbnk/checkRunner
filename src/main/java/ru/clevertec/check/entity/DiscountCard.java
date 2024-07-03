package ru.clevertec.check.entity;


import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class DiscountCard {
    private int id;
    private String number;
    private double discountAmount;
}