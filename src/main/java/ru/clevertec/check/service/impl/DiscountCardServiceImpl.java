package ru.clevertec.check.service.impl;

import ru.clevertec.check.dao.DiscountCardDao;
import ru.clevertec.check.dao.impl.DiscountCardDaoImpl;
import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.util.CsvUtils;
import ru.clevertec.check.util.impl.CsvUtilsImpl;

import java.sql.Connection;
import java.util.*;

public class DiscountCardServiceImpl implements DiscountCardService {
    private Map<String, DiscountCard> discountCards = new HashMap<>();
    private Connection connection;

    public DiscountCardServiceImpl(String discountCardFilePath) {
        loadDiscountCards(discountCardFilePath);
    }

    public DiscountCardServiceImpl(Connection connection) {
        this.connection = connection;
    }

    public void loadDiscountCardsFromDb() {
        DiscountCardDao discountCardDao = DiscountCardDaoImpl.getInstance();
        discountCards = discountCardDao.getAll(connection);
    }

    public Set<String> getDiscountCardNumbers() {
        return Collections.unmodifiableSet(discountCards.keySet());
    }

    public DiscountCard getDiscountCardByNumber(String number) {
        return discountCards.get(number);
    }

    private void loadDiscountCards(String filePath) {
        CsvUtils csvUtils = new CsvUtilsImpl();
        List<String[]> lines = csvUtils.readCSV(filePath);
        for (int i = 1; i < lines.size(); i++) {
            String[] line = lines.get(i);
            int id = Integer.parseInt(line[0]);
            String number = line[1];
            double discountAmount = Double.parseDouble(line[2]);
            discountCards.put(number, DiscountCard.builder()
                    .id(id)
                    .number(number)
                    .discountAmount(discountAmount)
                    .build());
        }
    }
}
