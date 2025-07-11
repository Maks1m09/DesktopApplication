package by.taskSetter.utils;

import java.io.IOException;
import java.util.logging.*;

public class LoggerConfigurator {

    public static void setup() {
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.INFO);
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            logger.removeHandler(handler);
        }
        try {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);
            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setLevel(Level.INFO);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




