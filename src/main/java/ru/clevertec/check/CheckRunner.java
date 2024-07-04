package ru.clevertec.check;

import ru.clevertec.check.factory.ServiceFactory;
import ru.clevertec.check.factory.impl.ServiceFactoryImpl;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.validator.CustomValidator;
import ru.clevertec.check.validator.impl.ArgsValidator;

public class CheckRunner {
    public static final String PRODUCTS_PATH = "./src/main/resources/products.csv";
    public static final String DISCOUNT_CARDS_PATH = "./src/main/resources/discountCards.csv";
    public static final String WRITE_CHECK_PATH = "./result.csv";

    public static void main(String[] args) {
        CustomValidator<String[]> validator = ArgsValidator.INSTANCE;
        validator.validate(args);

        ServiceFactory serviceFactory = ServiceFactoryImpl.INSTANCE;
        ProductService productService = serviceFactory.createProductService(PRODUCTS_PATH);
        DiscountCardService discountCardService = serviceFactory.createDiscountCardService(DISCOUNT_CARDS_PATH);
        CheckService checkService = serviceFactory.createCheckService(productService, discountCardService);

        checkService.processArgs(args);
        checkService.calculateTotalSum();
    }
}