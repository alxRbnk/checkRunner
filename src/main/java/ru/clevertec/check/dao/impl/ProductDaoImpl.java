package ru.clevertec.check.dao.impl;

import ru.clevertec.check.command.PrintCommandType;
import ru.clevertec.check.dao.ProductDao;
import ru.clevertec.check.entity.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static ru.clevertec.check.command.PrintCommandType.BAD_REQUEST;

public class ProductDaoImpl implements ProductDao {
    private static final String SELECT_ALL = "SELECT * FROM product;";
    private static ProductDaoImpl instance = new ProductDaoImpl();

    private ProductDaoImpl() {
    }

    public static ProductDaoImpl getInstance() {
        return instance;
    }

    @Override
    public Map<Integer, Product> getAll(Connection connection) {
        Map<Integer, Product> items = new HashMap<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int quantityInStock = resultSet.getInt("quantity_in_stock");
                boolean isWholesaleProduct = resultSet.getBoolean("wholesale_product");
                Product product = Product.builder()
                        .id(id)
                        .description(description)
                        .price(BigDecimal.valueOf(price))
                        .quantityInStock(quantityInStock)
                        .isWholesale(isWholesaleProduct)
                        .build();
                items.put(id, product);
            }
        } catch (SQLException e) {
            PrintCommandType.define(BAD_REQUEST).execute();
            throw new RuntimeException(e);
        }
        return items;
    }
}
