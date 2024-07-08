package ru.clevertec.check;

import ru.clevertec.check.factory.ServiceFactory;
import ru.clevertec.check.factory.impl.ServiceFactoryImpl;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.util.ArgsProcessor;
import ru.clevertec.check.util.impl.ArgsProcessorImpl;
import ru.clevertec.check.validator.CustomValidator;
import ru.clevertec.check.validator.impl.ExtendedArgsValidatorImpl;

public class CheckRunner {
    public static final String DISCOUNT_CARDS_PATH = "./src/main/resources/discountCards.csv";
    public static final String DEFAULT_CHECK_PATH = "./result.csv";

    public static void main(String[] args) {
        CustomValidator<String[]> validator = ExtendedArgsValidatorImpl.INSTANCE;
        validator.validate(args);

        ArgsProcessor argsProcessor = new ArgsProcessorImpl(args);
        String productPath = argsProcessor.getProductPath();
        String saveToFilePath = argsProcessor.getSaveToFilePath();

        ServiceFactory serviceFactory = ServiceFactoryImpl.INSTANCE;
        ProductService productService = serviceFactory.createProductService(productPath);
        DiscountCardService discountCardService = serviceFactory.createDiscountCardService(DISCOUNT_CARDS_PATH);
        CheckService checkService = serviceFactory.createCheckService(productService, discountCardService);

        checkService.processArgs(args);
        checkService.calculateTotalSum();
        checkService.printCheckToConsole();
        checkService.printCheckToCsv(saveToFilePath);
    }
}
