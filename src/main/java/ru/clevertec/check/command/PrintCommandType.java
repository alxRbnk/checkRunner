package ru.clevertec.check.command;

import ru.clevertec.check.command.impl.InternalServerError;
import ru.clevertec.check.command.impl.PrintBadRequest;
import ru.clevertec.check.command.impl.PrintNonEnoughMoney;

public enum PrintCommandType {
    BAD_REQUEST(new PrintBadRequest()),
    NOT_ENOUGH_MONEY(new PrintNonEnoughMoney()),
    INTERNAL_SERVER_ERROR(new InternalServerError());

    final PrintErrorCommand printCommand;

    PrintCommandType(PrintErrorCommand printCommand) {
        this.printCommand = printCommand;
    }

    public static PrintErrorCommand define(PrintCommandType type) {
        return type.printCommand;
    }
}
