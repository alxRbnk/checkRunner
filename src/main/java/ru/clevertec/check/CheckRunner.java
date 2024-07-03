package ru.clevertec.check;

import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.service.impl.CheckServiceImpl;
import ru.clevertec.check.service.impl.DiscountCardServiceImpl;
import ru.clevertec.check.service.impl.ProductServiceImpl;
import ru.clevertec.check.validator.CustomValidator;

import java.math.BigDecimal;

public class CheckRunner {
    public static final String PRODUCTS_PATH = "./src/main/resources/products.csv";
    public static final String DISCOUNT_CARDS_PATH = "./src/main/resources/discountCards.csv";
    public static final String OUTPUT_CHECK_PATH = "./result.csv";

    public static void main(String[] args) {
        CustomValidator customValidator = new CustomValidator();
        customValidator.validateArgs(args);
        ProductService productService = new ProductServiceImpl(PRODUCTS_PATH);
        DiscountCardService discountCardService = new DiscountCardServiceImpl(DISCOUNT_CARDS_PATH);
        CheckService checkService = new CheckServiceImpl(productService, discountCardService, OUTPUT_CHECK_PATH);

        for (String arg : args) {
            if (arg.startsWith("discountCard=")) {
                checkService.applyDiscountCard(arg.split("=")[1]);
            } else if (arg.startsWith("balanceDebitCard=")) {
                checkService.setBalanceDebitCard(BigDecimal.valueOf(Double.parseDouble(arg.split("=")[1])));//fixme
            } else if (arg.contains("-")) {
                String[] parts = arg.split("-");
                int productId = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);
                checkService.addItem(productId, quantity);
            }
        }

        checkService.calculateTotalSum();
        System.out.println(checkService.getBalanceDebitCard());

    }
}