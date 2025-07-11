package by.taskSetter;

import by.taskSetter.ui.TaskApp;
import by.taskSetter.utils.LoggerConfigurator;

import javax.swing.*;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {

        LoggerConfigurator.setup();

        SwingUtilities.invokeLater(() -> {
            Logger.getLogger(Main.class.getName()).info("Запуск приложения");
            new TaskApp().setVisible(true);
        });
    }
}
