package ru.clevertec.check.service.impl;

import ru.clevertec.check.entity.DiscountCard;
import ru.clevertec.check.service.DiscountCardService;
import ru.clevertec.check.util.CsvUtils;
import ru.clevertec.check.util.impl.CsvUtilsImpl;

import java.util.*;

public class DiscountCardServiceImpl implements DiscountCardService {
    private final Map<String, DiscountCard> discountCards = new HashMap<>();

    public DiscountCardServiceImpl(String discountCardFilePath) {
        loadDiscountCards(discountCardFilePath);
    }

    private void loadDiscountCards(String filePath) {
        CsvUtils csvUtils = new CsvUtilsImpl();
        List<String[]> lines = csvUtils.readCSV(filePath);
            for (int i = 1; i < lines.size(); i++){
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

    public Set<String> getDiscountCardNumbers(){
        return Collections.unmodifiableSet(discountCards.keySet());
    }

    public DiscountCard getDiscountCardByNumber(String number) {
        return discountCards.get(number);
    }
}
