package ru.clevertec.check.connection;

import ru.clevertec.check.command.PrintCommandType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.clevertec.check.command.ErrorMessages.BAD_REQUEST;

public class ConnectionFactory { //fixme connection pool

    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            PrintCommandType.define(PrintCommandType.BAD_REQUEST).execute();
            throw new ExceptionInInitializerError(BAD_REQUEST + e.getMessage());
        }
    }

    public static Connection createConnection(String url, String user, String password) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            PrintCommandType.define(PrintCommandType.BAD_REQUEST).execute();
            throw new IllegalArgumentException(BAD_REQUEST + e.getMessage());
        }
        return connection;
    }
}

