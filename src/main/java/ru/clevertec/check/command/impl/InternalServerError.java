package ru.clevertec.check.command.impl;

import ru.clevertec.check.CheckRunner;
import ru.clevertec.check.command.ErrorMessages;
import ru.clevertec.check.command.PrintErrorCommand;

import java.io.FileWriter;
import java.io.IOException;

public class InternalServerError implements PrintErrorCommand, ErrorMessages {

    @Override
    public void execute() {
        execute(CheckRunner.DEFAULT_CHECK_PATH);
    }

    @Override
    public void execute(String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.append(ERROR)
                    .append("\n\n")
                    .append(SERVER_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(ERROR_WRITING + path, e);
        }
    }
}

