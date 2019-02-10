package com.company;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class CustomLogger {
    private final static Logger logger = Logger.getLogger(CustomLogger.class.getName());
    private static FileHandler fh = null;


    public CustomLogger(Argument argument) {
        File f = new File(argument.getLogFile());
        try {
            fh = new FileHandler(argument.getLogFile(), true);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        CustomFormatter formatter = new CustomFormatter();
        Logger l = Logger.getLogger("");
        fh.setFormatter(formatter);
        LogManager.getLogManager().reset();
        l.addHandler(fh);
        l.setLevel(Level.CONFIG);
    }

    public static Logger getLogger() {
        return logger;
    }
}