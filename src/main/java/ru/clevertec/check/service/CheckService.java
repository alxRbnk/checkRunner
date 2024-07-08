package ru.clevertec.check.service;

public interface CheckService {

    void calculateTotalSum();

    void processArgs(String[] args);

    void printCheckToCsv();

    void printCheckToCsv(String path);

    void printCheckToConsole();

}
