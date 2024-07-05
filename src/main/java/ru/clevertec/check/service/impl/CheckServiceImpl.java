package ru.clevertec.check.service.impl;

import lombok.extern.log4j.Log4j2;
import ru.clevertec.check.CheckRunner;
import ru.clevertec.check.command.ErrorMessages;
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

import static ru.clevertec.check.command.PrintCommandType.*;

@Log4j2
public class CheckServiceImpl implements CheckService {
    private final ProductService productService;
    private final DiscountCardService discountCardService;
    private final List<Item> items = new ArrayList<>();
    private DiscountCard discountCard;
    private BigDecimal balanceDebitCard;

    public CheckServiceImpl(ProductService productService,
                            DiscountCardService discountCardService) {
        this.productService = productService;
        this.discountCardService = discountCardService;
    }

    public void processArgs(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("discountCard=")) {
                applyDiscountCard(arg.split("=")[1]);
            } else if (arg.startsWith("balanceDebitCard=")) {
                double balance = Double.parseDouble(arg.split("=")[1]);
                setBalanceDebitCard(BigDecimal.valueOf(balance));
            } else if (arg.contains("-")) {
                String[] parts = arg.split("-");
                int productId = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);
                addItem(productId, quantity);
            }
        }
    }

    public void calculateTotalSum() {
        if (balanceDebitCard == null) {
            PrintCommandType.define(BAD_REQUEST).execute();
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST);
        }
        BigDecimal totalSumWithDiscount = getTotalSum().subtract(getTotalDiscount());
        if (balanceDebitCard.subtract(totalSumWithDiscount).compareTo(BigDecimal.ZERO) < 0) {
            PrintCommandType.define(NOT_ENOUGH_MONEY).execute();
            throw new IllegalArgumentException(ErrorMessages.NOT_ENOUGH_MONEY);
        }
        balanceDebitCard = balanceDebitCard.subtract(totalSumWithDiscount);
        printCheckToCsv();
        printCheckToConsole();
    }

    private List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    private BigDecimal getTotalSum() {
        return items.stream()
                .map(Item::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getTotalDiscount() {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (Item item : items) {
            BigDecimal discount = getItemDiscount(item);
            totalDiscount = totalDiscount.add(discount);
        }
        return totalDiscount;
    }

    private BigDecimal getBalanceDebitCard() {
        return CustomRound.round(balanceDebitCard);
    }

    private BigDecimal getItemDiscount(Item item) {
        BigDecimal cardDiscount = BigDecimal.ZERO;
        BigDecimal discount;
        if (discountCard != null) {
            cardDiscount = BigDecimal.valueOf(discountCard.getDiscountAmount());
        }
        if (item.getProduct().isWholesale() && item.getQuantity() >= 5) {
            discount = item.getTotalPrice().multiply(BigDecimal.valueOf(0.1));
        } else if (discountCard == null) {
            return BigDecimal.ZERO;
        } else {
            discount = item.getTotalPrice()
                    .multiply(cardDiscount)
                    .divide(BigDecimal.valueOf(100));
        }
        return CustomRound.round(discount);
    }

    private void addItem(int productId, int quantity) {
        Product product = productService.getProductById(productId);
        if (product != null && product.getQuantityInStock() >= quantity) {
            items.add(Item.builder()
                    .product(product)
                    .quantity(quantity)
                    .build());
        } else {
            PrintCommandType.define(BAD_REQUEST).execute();
            throw new IllegalArgumentException(ErrorMessages.BAD_REQUEST);
        }
    }

    private void applyDiscountCard(String discountCardNumber) {
        if (discountCardService.getDiscountCardNumbers().contains(discountCardNumber)) {
            this.discountCard = discountCardService.getDiscountCardByNumber(discountCardNumber);
        } else {
            this.discountCard = DiscountCard.builder()
                    .number(discountCardNumber)
                    .discountAmount(2)
                    .build();
        }
    }

    private void setBalanceDebitCard(BigDecimal balanceDebitCard) {
        this.balanceDebitCard = balanceDebitCard;
    }

    private void printCheckToConsole() {
        log.info("Check:");
        for (Item item : getItems()) {
            log.info(item.getProduct().getDescription() + " - "
                    + item.getQuantity() + " pcs. - " + item.getTotalPrice() + " $");
        }
        log.info("Total amount: " + getTotalSum() + " $");
        log.info("Discount: " + getTotalDiscount() + " $");
        log.info("Total sum with discount: " + getTotalSum().subtract(getTotalDiscount()) + "$");
        log.info("Current balance: " + getBalanceDebitCard());
    }

    private void printCheckToCsv() {
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yy"));
        String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        try (FileWriter writer = new FileWriter(CheckRunner.WRITE_CHECK_PATH)) {
            writer.append("Date;Time\n")
                    .append(formattedDate).append(";").append(formattedTime).append("\n\n")
                    .append("QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n");
            for (Item item : getItems()) {
                writer.append(String.valueOf(item.getQuantity())).append(";")
                        .append(item.getProduct().getDescription()).append(";")
                        .append(CustomRound.round(item.getProduct().getPrice()) + "$")
                        .append(";")
                        .append(getItemDiscount(item) + "$").append(";")
                        .append(CustomRound.round(item.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))) + "$")
                        .append("\n");
            }
            if (discountCard != null) {
                writer.append("\n")
                        .append("DISCOUNT CARD;DISCOUNT PERCENTAGE\n")
                        .append(discountCard.getNumber()).append(";")
                        .append((int) discountCard.getDiscountAmount() + "%\n");
            }
            writer.append("\n")
                    .append("TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n")
                    .append(getTotalSum() + "$").append(";")
                    .append(getTotalDiscount() + "$").append(";")
                    .append(getTotalSum().subtract(getTotalDiscount()) + "$");
        } catch (IOException e) {
            PrintCommandType.define(INTERNAL_SERVER_ERROR).execute();
            throw new RuntimeException(ErrorMessages.ERROR_WRITING +
                    CheckRunner.WRITE_CHECK_PATH, e);
        }
    }
}