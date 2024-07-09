package ru.clevertec.check.factory;

import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.ProductService;

import java.sql.Connection;

public interface ServiceFactory {

    ProductService createProductService(String productsPath);

    DiscountCardService createDiscountCardService(String discountCardsPath);

    CheckService createCheckService(ProductService productService, DiscountCardService discountCardService);

    ProductService createProductServiceDb(Connection connection);

    DiscountCardService createDiscountCardServiceDb(Connection connection);
}
