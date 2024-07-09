package ru.clevertec.check.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.command.ErrorMessages;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.util.CustomRound;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CheckServiceImplTest {

    @Mock
    private ProductService productService;

    @Mock
    private DiscountCardService discountCardService;

    private CheckServiceImpl checkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        checkService = new CheckServiceImpl(productService, discountCardService);
    }

    @Test
    void addItem_ProductInStock_AddsItemToList() {
        int productId = 1;
        int quantity = 3;
        Product product = Product.builder()
                .id(productId)
                .description("Test Product")
                .price(BigDecimal.TEN)
                .quantityInStock(quantity + 2)
                .build();
        when(productService.getProductById(productId)).thenReturn(product);
        checkService.addItem(productId, quantity);
        assertEquals(1, checkService.getItems().size());
        assertEquals(productId, checkService.getItems().get(0).getProduct().getId());
        assertEquals(quantity, checkService.getItems().get(0).getQuantity());
    }

    @Test
    void addItem_ProductNotInStock_ThrowsException() {
        int productId = 1;
        int quantity = 5;
        Product product = Product.builder()
                .id(productId)
                .description("Test Product")
                .price(BigDecimal.TEN)
                .quantityInStock(quantity - 1)
                .build();
        when(productService.getProductById(productId)).thenReturn(product);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            checkService.addItem(productId, quantity);
        });
        assertEquals(ErrorMessages.BAD_REQUEST, exception.getMessage());
        assertTrue(checkService.getItems().isEmpty());
    }

    @Test
    void calculateTotalSum_EnoughBalance_UpdatesBalance() {
        int productId = 1;
        int quantity = 2;
        BigDecimal price = BigDecimal.TEN;
        Product product = Product.builder()
                .id(productId)
                .description("Test Product")
                .price(price)
                .quantityInStock(quantity)
                .build();
        when(productService.getProductById(productId)).thenReturn(product);
        checkService.addItem(productId, quantity);
        checkService.setBalanceDebitCard(BigDecimal.valueOf(50));
        checkService.calculateTotalSum();
        BigDecimal expectedBalance = CustomRound.round(BigDecimal.valueOf(30));
        assertEquals(expectedBalance, checkService.getBalanceDebitCard());
    }

    @Test
    void calculateTotalSum_NotEnoughBalance_ThrowsException() {
        int productId = 1;
        int quantity = 3;
        BigDecimal price = BigDecimal.TEN;
        Product product = Product.builder()
                .id(productId)
                .description("Test Product")
                .price(price)
                .quantityInStock(quantity)
                .build();
        when(productService.getProductById(productId)).thenReturn(product);
        checkService.addItem(productId, quantity);
        checkService.setBalanceDebitCard(BigDecimal.valueOf(20));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            checkService.calculateTotalSum();
        });
        assertEquals(ErrorMessages.NOT_ENOUGH_MONEY, exception.getMessage());
    }


}