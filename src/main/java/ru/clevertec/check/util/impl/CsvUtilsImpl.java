package ru.clevertec.check.util.impl;

import ru.clevertec.check.util.CsvUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvUtilsImpl implements CsvUtils {

    public List<String[]> readCSV(String filePath) {
        List<String[]> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.split(";"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error when reading a file " + filePath, e);
        }
        return lines;
    }
}
