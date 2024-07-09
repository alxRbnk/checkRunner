package ru.clevertec.check;

import ru.clevertec.check.connection.ConnectionFactory;
import ru.clevertec.check.factory.ServiceFactory;
import ru.clevertec.check.factory.impl.ServiceFactoryImpl;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.util.DbArgsProcessor;
import ru.clevertec.check.util.impl.DbArgsProcessorImpl;
import ru.clevertec.check.validator.CustomValidator;
import ru.clevertec.check.validator.impl.DbArgsValidatorImpl;

import java.sql.Connection;
import java.sql.SQLException;

public class CheckRunner {
    public static final String DEFAULT_CHECK_PATH = "./result.csv";

    public static void main(String[] args) throws SQLException {
        CustomValidator<String[]> validator = DbArgsValidatorImpl.INSTANCE;
        validator.validate(args);

        DbArgsProcessor argsProcessor = new DbArgsProcessorImpl(args);
        String url = argsProcessor.getUrl();
        String user = argsProcessor.getUser();
        String password = argsProcessor.getPassword();
        String saveToFilePath = argsProcessor.getSavePath();

        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(url, user, password);
            ServiceFactory serviceFactory = ServiceFactoryImpl.INSTANCE;
            ProductService productService = serviceFactory.createProductServiceDb(connection);
            productService.loadProductFromDb();
            DiscountCardService discountCardService = serviceFactory.createDiscountCardServiceDb(connection);
            discountCardService.loadDiscountCardsFromDb();
            CheckService checkService = serviceFactory.createCheckService(productService, discountCardService);
            checkService.processArgs(args);
            checkService.calculateTotalSum();
            checkService.printCheckToConsole();
            checkService.printCheckToCsv(saveToFilePath);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
