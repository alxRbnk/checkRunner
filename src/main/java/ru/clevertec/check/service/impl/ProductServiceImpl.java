package ru.clevertec.check.service.impl;

import ru.clevertec.check.dao.ProductDao;
import ru.clevertec.check.dao.impl.ProductDaoImpl;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.util.CsvUtils;
import ru.clevertec.check.util.impl.CsvUtilsImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductServiceImpl implements ProductService {
    private Map<Integer, Product> products = new HashMap<>();
    private Connection connection;

    public ProductServiceImpl(String productFilePath) {
        loadProducts(productFilePath);
    }

    public ProductServiceImpl(Connection connection) {
        this.connection = connection;
    }

    public Product getProductById(int id) {
        return products.get(id);
    }

    public void loadProductFromDb() {
        ProductDao productDao = ProductDaoImpl.getInstance();
        products = productDao.getAll(connection);
    }

    private void loadProducts(String filePath) {
        CsvUtils csvUtils = new CsvUtilsImpl();
        List<String[]> lines = csvUtils.readCSV(filePath);
        for (int i = 1; i < lines.size(); i++) {
            String[] line = lines.get(i);
            int id = Integer.parseInt(line[0]);
            String description = line[1];
            double price = Double.parseDouble(line[2]);
            int quantityInStock = Integer.parseInt(line[3]);
            boolean isWholesale = Boolean.parseBoolean(line[4]);
            products.put(id, Product.builder()
                    .id(id)
                    .description(description)
                    .price(BigDecimal.valueOf(price))
                    .quantityInStock(quantityInStock)
                    .isWholesale(isWholesale)
                    .build());
        }
    }
}