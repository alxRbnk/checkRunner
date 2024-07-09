package ru.clevertec.check.util.impl;

import ru.clevertec.check.util.DbArgsProcessor;

public class DbArgsProcessorImpl implements DbArgsProcessor {
    private String url;
    private String user;
    private String password;
    private String saveToFilePath;

    public DbArgsProcessorImpl(String[] args) {
        processArgs(args);
    }

    private void processArgs(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("datasource.url=")) {
                url = arg.split("=")[1];
            } else if (arg.startsWith("datasource.username=")) {
                user = arg.split("=")[1];
            } else if (arg.startsWith("datasource.password=")) {
                password = arg.split("=")[1];
            } else if (arg.startsWith("saveToFile=")) {
                saveToFilePath = arg.split("=")[1];
            }
        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getSavePath() {
        return saveToFilePath;
    }
}
