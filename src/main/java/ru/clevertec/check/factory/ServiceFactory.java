package ru.clevertec.check.factory;

import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.ProductService;

public interface ServiceFactory {

    ProductService createProductService(String productsPath);

    DiscountCardService createDiscountCardService(String discountCardsPath);

    CheckService createCheckService(ProductService productService, DiscountCardService discountCardService);
}
