package ru.clevertec.check.factory.impl;

import ru.clevertec.check.factory.ServiceFactory;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.service.impl.CheckServiceImpl;
import ru.clevertec.check.service.impl.DiscountCardServiceImpl;
import ru.clevertec.check.service.impl.ProductServiceImpl;

public enum ServiceFactoryImpl implements ServiceFactory {
    INSTANCE;

    @Override
    public ProductService createProductService(String productsPath) {
        return new ProductServiceImpl(productsPath);
    }

    @Override
    public DiscountCardService createDiscountCardService(String discountCardsPath) {
        return new DiscountCardServiceImpl(discountCardsPath);
    }

    @Override
    public CheckService createCheckService(ProductService productService, DiscountCardService discountCardService) {
        return new CheckServiceImpl(productService, discountCardService);
    }
}
