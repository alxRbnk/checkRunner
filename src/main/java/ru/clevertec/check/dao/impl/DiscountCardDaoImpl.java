package ru.clevertec.check.dao.impl;

import ru.clevertec.check.command.PrintCommandType;
import ru.clevertec.check.dao.DiscountCardDao;
import ru.clevertec.check.entity.DiscountCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static ru.clevertec.check.command.PrintCommandType.BAD_REQUEST;

public class DiscountCardDaoImpl implements DiscountCardDao {
    private static final String SELECT_ALL = "SELECT * FROM discount_card;";
    private static DiscountCardDaoImpl instance = new DiscountCardDaoImpl();

    private DiscountCardDaoImpl() {
    }

    public static DiscountCardDaoImpl getInstance() {
        return instance;
    }

    @Override
    public Map<String, DiscountCard> getAll(Connection connection) {
        Map<String, DiscountCard> cards = new HashMap<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String number = resultSet.getString("number");
                double amount = resultSet.getDouble("amount");
                DiscountCard product = DiscountCard.builder()
                        .id(id)
                        .discountAmount(amount)
                        .number(number)
                        .build();
                cards.put(number, product);
            }
        } catch (SQLException e) {
            PrintCommandType.define(BAD_REQUEST).execute();
            throw new RuntimeException(e);
        }
        return cards;
    }
}
