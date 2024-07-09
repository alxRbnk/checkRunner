package ru.clevertec.check.dao;

import ru.clevertec.check.entity.Product;

import java.sql.Connection;
import java.util.Map;

public interface ProductDao {

    Map<Integer, Product> getAll(Connection connection);
}
