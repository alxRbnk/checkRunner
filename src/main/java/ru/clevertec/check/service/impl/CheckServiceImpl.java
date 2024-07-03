package ru.clevertec.check.service.impl;


import ru.clevertec.check.command.PrintCommand;
import ru.clevertec.check.command.PrintCommandType;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.entity.Item;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.util.CustomRound;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckServiceImpl implements CheckService {
    private final String filePath;
    private final ProductService productService;
    private final DiscountCardService discountCardService;
    private final List<Item> items = new ArrayList<>();
    private DiscountCard discountCard;
    private BigDecimal balanceDebitCard;

    public CheckServiceImpl(ProductService productService,
                            DiscountCardService discountCardService, String filePath) {
        this.productService = productService;
        this.discountCardService = discountCardService;
        this.filePath = filePath;
    }

    public void addItem(int productId, int quantity) {
        Product product = productService.getProductById(productId);
        if (product != null && product.getQuantityInStock() >= quantity) {
            items.add(Item.builder()
                    .product(product)
                    .quantity(quantity)
                    .build());
        } else {
            PrintCommandType.define(PrintCommandType.BAD_REQUEST).execute();
            throw new IllegalArgumentException(PrintCommand.BAD_REQUEST);
        }
    }

    public void applyDiscountCard(String discountCardNumber) {
        if (discountCardService.getDiscountCardNumbers().contains(discountCardNumber)) {
            this.discountCard = discountCardService.getDiscountCardByNumber(discountCardNumber);
        } else {
            this.discountCard = DiscountCard.builder()
                    .number(discountCardNumber)
                    .discountAmount(2)
                    .build();
        }
    }

    public void setBalanceDebitCard(BigDecimal balanceDebitCard) {
        this.balanceDebitCard = balanceDebitCard;
    }

    public void calculateTotalSum() {
        if (balanceDebitCard == null) {
            PrintCommandType.define(PrintCommandType.BAD_REQUEST).execute();
            throw new IllegalArgumentException(PrintCommand.BAD_REQUEST);
        }
        BigDecimal totalSumWithDiscount = getTotalSum().subtract(getTotalDiscount());
        if (balanceDebitCard.subtract(totalSumWithDiscount).compareTo(BigDecimal.ZERO) < 0) {
            PrintCommandType.define(PrintCommandType.NOT_ENOUGH_MONEY).execute();
            throw new IllegalArgumentException(PrintCommand.NOT_ENOUGH_MONEY);
        }
        balanceDebitCard = balanceDebitCard.subtract(totalSumWithDiscount);
        printCheckToCsv();
        printCheckToConsole();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public BigDecimal getTotalSum() {
        return items.stream()
                .map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalDiscount() {
        BigDecimal cardDiscount = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        if (discountCard != null) {
            cardDiscount = BigDecimal.valueOf(discountCard.getDiscountAmount());
        }
        for (Item item : items) {
            BigDecimal discount;
            if (item.getProduct().isWholesale() && item.getQuantity() >= 5) {
                discount = item.getTotalPrice().multiply(BigDecimal.valueOf(0.1));
            } else if (discountCard == null) {
                continue;
            } else {
                discount = item.getTotalPrice()
                        .multiply(cardDiscount)
                        .divide(BigDecimal.valueOf(100));
            }
            totalDiscount = totalDiscount.add(discount);
        }
        return CustomRound.round(totalDiscount);
    }

    public BigDecimal getBalanceDebitCard() {
        return CustomRound.round(balanceDebitCard);
    }

    private void printCheckToConsole() {
        System.out.println("Check:");
        for (Item item : getItems()) {
            System.out.println(item.getProduct().getDescription() + " - "
                    + item.getQuantity() + " pcs. - " + item.getTotalPrice() + " $");
        }
        System.out.println("Total amount: " + getTotalSum() + " $");
        System.out.println("Discount: " + getTotalDiscount() + " $");
        System.out.println("Total sum with discount: " + getTotalSum().subtract(getTotalDiscount()) + "$");
    }

    private void printCheckToCsv() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDate = LocalDate.now().format(dateFormatter);
        String formattedTime = LocalTime.now().format(timeFormatter);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Date;Time\n");
            writer.append(formattedDate + ";" + formattedTime + "\n\n");
            writer.append("QTY;DESCRIPTION;PRICE\n");
            for (Item item : getItems()) {
                writer.append(String.valueOf(item.getQuantity()))
                        .append(";")
                        .append(item.getProduct().getDescription())
                        .append(";")
                        .append(item.getTotalPrice() + "$")
                        .append("\n");
            }
            writer.append("\n");
            writer.append("TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n")
                    .append(getTotalSum() + "$")
                    .append(";");
            writer.append(getTotalDiscount() + "$")
                    .append(";");
            writer.append(getTotalSum().subtract(getTotalDiscount()) + "$");
        } catch (IOException e) {
            PrintCommandType.define(PrintCommandType.INTERNAL_SERVER_ERROR).execute();
            throw new RuntimeException(PrintCommand.ERROR_WRITING + filePath, e);
        }
    }
}