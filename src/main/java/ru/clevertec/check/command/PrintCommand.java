package ru.clevertec.check.command;

public interface PrintCommand {
    String NOT_ENOUGH_MONEY = "NOT ENOUGH MONEY";
    String BAD_REQUEST = "BAD REQUEST";
    String SERVER_ERROR = "INTERNAL SERVER ERROR";
    String ERROR = "ERROR";
    String ERROR_WRITING = "Error when writing a file ";

    void execute();
}
