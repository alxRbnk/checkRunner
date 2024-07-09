package ru.clevertec.check.dao;

import ru.clevertec.check.entity.DiscountCard;

import java.sql.Connection;
import java.util.Map;

public interface DiscountCardDao {

    Map<String, DiscountCard> getAll(Connection connection);
}
