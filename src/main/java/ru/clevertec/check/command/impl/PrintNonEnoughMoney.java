package ru.clevertec.check.command.impl;

import ru.clevertec.check.CheckRunner;
import ru.clevertec.check.command.ErrorMessages;
import ru.clevertec.check.command.PrintErrorCommand;

import java.io.FileWriter;
import java.io.IOException;

public class PrintNonEnoughMoney implements PrintErrorCommand, ErrorMessages {
    @Override
    public void execute() {
        try (FileWriter writer = new FileWriter(CheckRunner.WRITE_CHECK_PATH)) {
            writer.append(ERROR)
                    .append("\n\n")
                    .append(NOT_ENOUGH_MONEY);
        } catch (IOException e) {
            throw new RuntimeException(ERROR_WRITING + CheckRunner.WRITE_CHECK_PATH, e);
        }
    }
}
