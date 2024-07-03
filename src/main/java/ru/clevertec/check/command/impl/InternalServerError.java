package ru.clevertec.check.command.impl;

import ru.clevertec.check.CheckRunner;
import ru.clevertec.check.command.PrintCommand;

import java.io.FileWriter;
import java.io.IOException;

public class InternalServerError implements PrintCommand {
    @Override
    public void execute() {
        try (FileWriter writer = new FileWriter(CheckRunner.OUTPUT_CHECK_PATH)) {
            writer.append(ERROR);
            writer.append("\n\n");
            writer.append(SERVER_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(ERROR_WRITING + CheckRunner.OUTPUT_CHECK_PATH, e);
        }
    }
}
