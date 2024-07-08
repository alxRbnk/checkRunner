package ru.clevertec.check.util.impl;

import ru.clevertec.check.util.ArgsProcessor;

public class ArgsProcessorImpl implements ArgsProcessor {
    private String productPath;
    private String saveToFilePath;

    public ArgsProcessorImpl(String[] args) {
        processArgs(args);
    }

    private void processArgs(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("pathToFile=")) {
                productPath = arg.split("=")[1];
            } else if (arg.startsWith("saveToFile=")) {
                saveToFilePath = arg.split("=")[1];
            }
        }
    }

    @Override
    public String getProductPath() {
        return productPath;
    }

    @Override
    public String getSaveToFilePath() {
        return saveToFilePath;
    }
}
